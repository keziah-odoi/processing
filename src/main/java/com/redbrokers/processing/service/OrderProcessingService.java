package com.redbrokers.processing.service;

import com.redbrokers.processing.exceptions.EntityNotFoundException;
import com.redbrokers.processing.model.Order;
import com.redbrokers.processing.repository.OrderProcessingRepository;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class OrderProcessingService {
    OrderProcessingRepository orderProcessingRepository;
       public void createOrder( Order order) {
        orderProcessingRepository.save(order);
    }

    public void executeOrder() {
    }

    public void updateOrder( Order order) {
           orderProcessingRepository.save(order);
    }

    public Order getOrderById(UUID orderId) {
           return orderProcessingRepository.findById(orderId).orElseThrow(()-> new EntityNotFoundException("Order with id provided does not exist"));
    }

    public void cancelOrder( Order order) {

    }

    public ResponseEntity<?> getFullOrderBooks() {
           return null;
    }

    public ResponseEntity<?> getOrderBooksByTicker() {
           return null;
    }

    public ResponseEntity<?> getOrderBooksByTickerBought() {
           return null;
    }

    public ResponseEntity<?> getOrderBooksByTickerSold() {
           return null;
    }

    public ResponseEntity<?> getOrderBooksTickerOpen() {
           return null;
    }

    public ResponseEntity<?> getOrderBookTickerClosed() {
           return null;
    }

    public ResponseEntity<?> getOrderBookTickerCancelled() {
           return null;
    }
}
