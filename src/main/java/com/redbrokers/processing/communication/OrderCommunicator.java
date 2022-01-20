package com.redbrokers.processing.communication;

import com.redbrokers.processing.dto.OrderRequestBody;
import com.redbrokers.processing.dto.SingleOrder;
import com.redbrokers.processing.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderCommunicator {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookCommunicator.class);

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${order-processing.variables.urls.exchange-one}")
    private String exchangeOne;
    @Value("${order-processing.variables.urls.exchange-two}")
    private String exchangeTwo;
    @Value("${private-key}")
    private String privateKey;


    public String executeOrderRequest(OrderRequestBody body) {
        String exchangeOneURL = exchangeOne + "/" + privateKey + "/order";
//        System.out.println(exchangeOneURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderRequestBody> request =
                new HttpEntity<OrderRequestBody>(body, headers);
        String idFromExchange =
                restTemplate.postForObject(exchangeOneURL, request, String.class);
        return idFromExchange;
    }


    public ResponseEntity<String> updateOrderRequest(OrderRequestBody body, String orderIdFromExchange) {
        String exchangeOneURL = exchangeOne + "/" + privateKey + "/order/" + orderIdFromExchange;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderRequestBody> request = new HttpEntity<OrderRequestBody>(body, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(exchangeOneURL, HttpMethod.PUT, request, ParameterizedTypeReference.forType(String.class));
        return response;
    }

    public ResponseEntity<?> cancelOrderRequest(String id) {
        String exchangeOneURL = exchangeOne + "/" + privateKey + "/order/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderRequestBody> request =
                new HttpEntity<OrderRequestBody>(headers);
//        restTemplate.delete(exchangeOneURL, request);
        ResponseEntity<Boolean> response =
                restTemplate.exchange(exchangeOneURL, HttpMethod.DELETE, request, ParameterizedTypeReference.forType(Boolean.class));
        System.out.println(response);
        return response;
    }

    public ResponseEntity<?> getOrderByIdRequest(String id) {
        String exchangeOneURL = exchangeOne + "/" + privateKey + "/order/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderRequestBody> request =
                new HttpEntity<OrderRequestBody>(headers);
        ResponseEntity<?> response =restTemplate.exchange(exchangeOneURL, HttpMethod.GET, request, ParameterizedTypeReference.forType(SingleOrder.class));
        return response;
    }
}
