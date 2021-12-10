package com.redbrokers.processing.controller;

import com.redbrokers.processing.OrderProcessing;
import com.redbrokers.processing.dto.OrderRequestBody;
import com.redbrokers.processing.service.OrderProcessingService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/orders")
public class OrderProcessingController {

    private OrderProcessingService orderProcessingService;

    @Autowired
    public OrderProcessingController(OrderProcessingService orderProcessingService) {
    }

    @PostMapping("/create")
    public void processOrderHandler(@RequestBody OrderRequestBody orderRequestBody) {
        System.out.println(orderRequestBody);

   }



}
