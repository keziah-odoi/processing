package com.redbrokers.processing.communication;

import com.redbrokers.processing.dto.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class MarketDataCommunicator {
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${order-processing.variables.urls.exchange-one}")
    private String exchangeOne;
    @Value("${order-processing.variables.urls.exchange-two}")
    private String exchangeTwo;

    public List<Product> getProductsFromExchange() {
        String exchangeOneURL = exchangeOne + "/md";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Product[] products = restTemplate.getForObject(exchangeOneURL, Product[].class);
        return Arrays.asList(products);
    }
}
