package com.redbrokers.processing;

import com.redbrokers.processing.enums.Side;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OrderProcessing {

    public static void main(String[] args) {
        SpringApplication.run(OrderProcessing.class, args);

    }

}
