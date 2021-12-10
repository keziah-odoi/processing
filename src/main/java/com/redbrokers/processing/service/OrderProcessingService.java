package com.redbrokers.processing.service;

import com.redbrokers.processing.communication.OrderCommunicator;
import com.redbrokers.processing.dto.OrderRequestBody;
import com.redbrokers.processing.exceptions.EntityNotFoundException;
import com.redbrokers.processing.model.Order;
import com.redbrokers.processing.repository.OrderProcessingRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
//@NoArgsConstructor
@RequiredArgsConstructor
public class OrderProcessingService {
    private final OrderCommunicator communicator;
    private final   OrderProcessingRepository orderProcessingRepository;

//    public OrderProcessingService(OrderCommunicator communicator) {
//        this.communicator = communicator;
//    }


//    OrderProcessingRepository orderProcessingRepository;
//       public void createOrder( Order order) {
//        orderProcessingRepository.save(order);
//    }

    public ResponseEntity<?> executeOrder(OrderRequestBody orderRequestBody) {
        String idFromExchange = communicator.executeOrderRequest(orderRequestBody);
        return new ResponseEntity<>(idFromExchange.replaceAll("^\"|\"$", ""), HttpStatus.OK);
    }

    public ResponseEntity<?> updateOrder(OrderRequestBody orderRequestBody, String id) {
        ResponseEntity<String> responseFromExhange = communicator.updateOrderRequest(orderRequestBody, id);
        return responseFromExhange;
    }

    public void deleteOrder(String id) {
        communicator.deleteOrderRequest(id);
    }

//
//    public Order getOrderById(String orderId) {
//           return orderProcessingRepository.findById(UUID.fromString(orderId)).orElseThrow(()-> new EntityNotFoundException("Order with id provided does not exist"));
//    }
//
//    public void cancelOrder( Order order) {
//
//    }


}
