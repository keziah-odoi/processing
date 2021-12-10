package com.redbrokers.processing.repository;

import com.redbrokers.processing.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderProcessingRepository extends JpaRepository<Order, UUID> {
}
