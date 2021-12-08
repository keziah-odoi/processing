package com.redbrokers.processing.service;

import com.redbrokers.processing.model.Order;
import com.redbrokers.processing.repository.OrderRepository;

import java.util.Optional;

public class OrderService {
    OrderRepository orderRepository ;
       public void createOrder( Order order) {
        orderRepository.save(order);
    }

    public void executeOrder() {
    }

    public void updateOrder( Order order) {
           orderRepository.save(order);
    }

    public Optional<Order> getOrder(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public void cancelOrder( Order order) {

    }
}
