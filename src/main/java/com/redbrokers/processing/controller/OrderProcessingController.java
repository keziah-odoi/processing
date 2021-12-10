package com.redbrokers.processing.controller;

import com.redbrokers.processing.OrderProcessing;
import com.redbrokers.processing.dto.OrderRequestBody;
import com.redbrokers.processing.service.OrderProcessingService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderProcessingController {

    private final OrderProcessingService orderProcessingService;

//    @Autowired
//    public OrderProcessingController(OrderProcessingService orderProcessingService) {
//        this.orderProcessingService = orderProcessingService;
//    }

    @PostMapping("/create")
    public ResponseEntity<?> processOrderHandler(@RequestBody OrderRequestBody orderRequestBody) {
        System.out.println(orderRequestBody);
        var responseFromExchange = orderProcessingService.executeOrder(orderRequestBody);
        System.out.println(responseFromExchange);
        return responseFromExchange;
   }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateOrderHandler(@RequestBody OrderRequestBody orderRequestBody, @PathVariable("orderId") String idFromExchange) {
        System.out.println(idFromExchange);
        var responseFromExchange = orderProcessingService.updateOrder(orderRequestBody, idFromExchange);
        System.out.println(responseFromExchange);
        System.out.println(responseFromExchange.getStatusCode());
        return responseFromExchange;

    }



    @DeleteMapping("/delete{orderId}")
    public void deleteOrderHandler(@PathVariable String idFromExchange) {
        orderProcessingService.deleteOrder(idFromExchange);
        System.out.println(idFromExchange);

    }


}
