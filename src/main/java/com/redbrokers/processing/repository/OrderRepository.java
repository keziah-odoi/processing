package com.redbrokers.processing.repository;

import com.redbrokers.processing.model.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long > {
}
