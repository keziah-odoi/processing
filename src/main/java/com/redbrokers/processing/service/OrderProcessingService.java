package com.redbrokers.processing.service;

import com.redbrokers.processing.communication.MarketDataCommunicator;
import com.redbrokers.processing.communication.OrderBookCommunicator;
import com.redbrokers.processing.communication.OrderCommunicator;
import com.redbrokers.processing.dto.GroupedOrder;
import com.redbrokers.processing.dto.OrderRequestBody;
import com.redbrokers.processing.dto.Product;
import com.redbrokers.processing.dto.SingleOrder;
import com.redbrokers.processing.enums.Side;
import com.redbrokers.processing.model.Order;
import com.redbrokers.processing.repository.OrderProcessingRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.annotation.PostConstruct;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;


@Service
@NoArgsConstructor
public class OrderProcessingService {
    private OrderCommunicator communicator;
    private OrderProcessingRepository orderProcessingRepository;
    private OrderBookCommunicator orderBookCommunicator;
    private MarketDataCommunicator marketDataCommunicator;
    private Map<String, GroupedOrder> productOrdersCache = new HashMap<>();
    private List<Product> products = new ArrayList<>();
    // private UUID clientId = UUID.fromString("b60479c7-b274-4110-b381-eeaca145a824");

    @Autowired
    public OrderProcessingService(OrderCommunicator communicator, OrderProcessingRepository orderProcessingRepository, OrderBookCommunicator orderBookCommunicator, MarketDataCommunicator marketDataCommunicator) {
        this.communicator = communicator;
        this.orderProcessingRepository = orderProcessingRepository;
        this.orderBookCommunicator = orderBookCommunicator;
        this.marketDataCommunicator = marketDataCommunicator;
    }


    public Order createOrder(Order order) {
        return this.orderProcessingRepository.save(order);
    }


    public Iterable<SingleOrder> getOrdersBySideAndStatus(String url, String side, String product) throws HttpClientErrorException, HttpServerErrorException {
        return orderBookCommunicator.getOrdersBySideAndStatus(url, product, side);

    }

    @PostConstruct
    public void openProductsFromExchange() {
        this.products.addAll(marketDataCommunicator.getProductsFromExchange());
    }

    @Scheduled(initialDelay = 100, fixedDelay = 1000)
    public void populateOrderBookCache() {
        System.out.println("populating order book cache");
        for (Product product : this.products) {
            System.out.println("populating : " + product.getTicker());
            GroupedOrder groupedOrder = new GroupedOrder();
            groupedOrder.setTicker(product.getTicker());

            List<SingleOrder> ordersByTickerAndStatus = (List<SingleOrder>) getOrdersBySideAndStatus("https://exchange.matraining.com", "open", product.getTicker());
            Map<Side, List<SingleOrder>> groupedOrdersBySide = ordersByTickerAndStatus.stream().collect(groupingBy(SingleOrder::getSide));

            groupedOrder.setSingleOrdersBuy(groupedOrdersBySide.get(Side.BUY));
            groupedOrder.setSingleOrdersSell(groupedOrdersBySide.get(Side.SELL));
            this.productOrdersCache.put(product.getTicker(), groupedOrder);

        }
    }

    public Double computeMinBidPriceForProduct(String product) {
        OptionalDouble minPrice = this.productOrdersCache.get(product).getSingleOrdersBuy().stream().mapToDouble(SingleOrder::getPrice).min();
        return minPrice.orElse(0.0);
    }

    public Double computeMaxAskPriceForProduct(String product) {
        OptionalDouble maxPrice = this.productOrdersCache.get(product).getSingleOrdersSell().stream().mapToDouble( SingleOrder::getPrice).max();
        return maxPrice.orElse(0.0);
    }


    public ResponseEntity<?> executeOrder(OrderRequestBody orderRequestBody) {
        String idFromExchange = communicator.executeOrderRequest(orderRequestBody);
        System.out.println(idFromExchange);
        idFromExchange = idFromExchange.replaceAll("^\"|\"$", "");
        Order order = new Order();
        order.setSide(orderRequestBody.getSide().toString());
        order.setProduct(orderRequestBody.getProduct());
        order.setQuantity(orderRequestBody.getQuantity());
        order.setClientId(orderRequestBody.getClientId());
        order.setOrderIdFromExchange(idFromExchange);
        if (orderRequestBody.getSide() == Side.BUY) {
            order.setPrice(computeMinBidPriceForProduct(orderRequestBody.getProduct()));
        }
        else {
            order.setPrice(computeMaxAskPriceForProduct(order.getProduct()));
        }
        createOrder(order);
        return new ResponseEntity<>(idFromExchange, HttpStatus.OK);
    }

    public ResponseEntity<?> updateOrder(OrderRequestBody orderRequestBody, String id) {
        ResponseEntity<String> responseFromExchange = communicator.updateOrderRequest(orderRequestBody, id);
        return responseFromExchange;
    }

    public ResponseEntity<?> cancelOrder(String id) {
        return communicator.cancelOrderRequest(id);

    }

    public ResponseEntity<?> getOrderById(String orderId) {
        return communicator.getOrderByIdRequest(orderId);
    }


}
