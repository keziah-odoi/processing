package com.redbrokers.processing.controller;

import com.redbrokers.processing.service.OrderProcessingService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orderbook")
public class OrderProcessingController {

    private OrderProcessingService orderProcessingService;
//    @Autowired
//    public OrderProcessingController(OrderProcessingService orderProcessingService) {
//
//    }

    @GetMapping("/")
    public ResponseEntity<?> getFullOrderBooks() {
        return orderProcessingService.getFullOrderBooks();
    }

    @GetMapping("/{product}")
    public ResponseEntity<?> getOrderBookByTicker(@PathVariable("product") String ticker){
        return orderProcessingService.getOrderBooksByTicker();
    }

    @GetMapping("/{product}/buy")
    public ResponseEntity<?> getOrderBookByTickerBought(@PathVariable("product") String ticker){
        return orderProcessingService.getOrderBooksByTickerBought();
    }

    @GetMapping("/{product}/sell")
    public ResponseEntity<?> getOrderBookByTickerSold(@PathVariable("product") String ticker){
        return orderProcessingService.getOrderBooksByTickerSold();
    }

    @GetMapping("/{product}/open")
    public ResponseEntity<?> getOrderBookOpen(@PathVariable("product") String ticker){
        return orderProcessingService.getOrderBooksTickerOpen();
    }

    @GetMapping("/{product}/closed")
    public ResponseEntity<?> getOrderBookClosed(@PathVariable("product") String ticker){
        return orderProcessingService.getOrderBookTickerClosed();
    }

    @GetMapping("/{product}/cancelled")
    public ResponseEntity<?> getOrderBookCancelled(@PathVariable("product") String ticker){
        return orderProcessingService.getOrderBookTickerCancelled();
    }


}
