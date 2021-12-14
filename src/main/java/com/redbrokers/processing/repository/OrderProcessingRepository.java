package com.redbrokers.processing.repository;

import com.redbrokers.processing.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderProcessingRepository extends JpaRepository<Order, UUID> {
    @Query(value = "SELECT * FROM kez where status = ?1", nativeQuery = true)
    List<Order> getOrdersByStatus(String orderStatus);


}
