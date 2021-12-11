package com.redbrokers.processing.controller;

import com.redbrokers.processing.dto.OrderRequestBody;
import com.redbrokers.processing.model.Order;
import com.redbrokers.processing.service.OrderProcessingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/orders")
@AllArgsConstructor
public class OrderProcessingController {

    @Autowired
    private final OrderProcessingService orderProcessingService;

    @PostMapping("/")
    public ResponseEntity<?> processOrderHandler(@RequestBody OrderRequestBody orderRequestBody) {
        System.out.println(orderRequestBody);
        var responseFromExchange = orderProcessingService.executeOrder(orderRequestBody);
        System.out.println(responseFromExchange);
        return responseFromExchange;
   }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrderHandler(@RequestBody OrderRequestBody orderRequestBody, @PathVariable("orderId") String idFromExchange) {
        System.out.println(idFromExchange);
        var responseFromExchange = orderProcessingService.updateOrder(orderRequestBody, idFromExchange);
        System.out.println(responseFromExchange);
        System.out.println(responseFromExchange.getStatusCode());
        return responseFromExchange;
    }


    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrderHandler(@PathVariable("orderId") String idFromExchange) {
        var responseFromExchange = orderProcessingService.cancelOrder(idFromExchange);
        System.out.println(responseFromExchange);
        System.out.println(responseFromExchange.getStatusCode());
        return responseFromExchange;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderByIdHandler(@PathVariable("orderId") String idFromExchange) {
        System.out.println(idFromExchange);
        var responseFromExchange = orderProcessingService.getOrderById(idFromExchange);
        System.out.println(responseFromExchange);
        return responseFromExchange;
    }

}
