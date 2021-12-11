package com.redbrokers.processing.service;

import com.redbrokers.processing.communication.OrderBookCommunicator;
import com.redbrokers.processing.communication.OrderCommunicator;
import com.redbrokers.processing.dto.OrderRequestBody;
import com.redbrokers.processing.dto.SingleOrder;
import com.redbrokers.processing.exceptions.EntityNotFoundException;
import com.redbrokers.processing.model.Order;
import com.redbrokers.processing.repository.OrderProcessingRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.UUID;


@Service
@NoArgsConstructor
public class OrderProcessingService {
    private OrderCommunicator communicator;
    private OrderProcessingRepository orderProcessingRepository;
    private OrderBookCommunicator orderBookCommunicator;
   // private UUID clientId = UUID.fromString("b60479c7-b274-4110-b381-eeaca145a824");

    @Autowired
    public OrderProcessingService(OrderCommunicator communicator,  OrderProcessingRepository orderProcessingRepository, OrderBookCommunicator orderBookCommunicator) {
        this.communicator = communicator;
        this.orderProcessingRepository = orderProcessingRepository;
        this.orderBookCommunicator = orderBookCommunicator;
    }


       public Order createOrder(Order order) {
        this.orderProcessingRepository.save(order);
        return order;
    }

    public Iterable<SingleOrder> getOrdersBySideOrStatus(String url, String side, String product) throws HttpClientErrorException , HttpServerErrorException {
        return  orderBookCommunicator.getOrdersBySideOrStatus(url, product, side);

    }

    public ResponseEntity<?> executeOrder(OrderRequestBody orderRequestBody) {
        String idFromExchange = communicator.executeOrderRequest(orderRequestBody);
        System.out.println(idFromExchange);
        idFromExchange = idFromExchange.replaceAll("^\"|\"$", "");
        Order order = new Order();
        order.setPrice(orderRequestBody.getPrice());
        order.setSide(orderRequestBody.getSide().toString());
        order.setProduct(orderRequestBody.getProduct());
        order.setQuantity(orderRequestBody.getQuantity());
        order.setClientId(orderRequestBody.getClientId());
        order.setOrderIdFromExchange(idFromExchange);
        System.out.println(order);
        createOrder(order);
        System.out.println(getOrdersBySideOrStatus("https://exchange.matraining.com", "closed", "AAPL"));
        return new ResponseEntity<>(idFromExchange, HttpStatus.OK);
    }

    public ResponseEntity<?> updateOrder(OrderRequestBody orderRequestBody, String id) {
        ResponseEntity<String> responseFromExchange = communicator.updateOrderRequest(orderRequestBody, id);
        return responseFromExchange;
    }

    public ResponseEntity<?> cancelOrder(String id) {
        return communicator.cancelOrderRequest(id);

    }

    public ResponseEntity<?> getOrderById(String orderId) {
           return communicator.getOrderByIdRequest(orderId);
    }



}
