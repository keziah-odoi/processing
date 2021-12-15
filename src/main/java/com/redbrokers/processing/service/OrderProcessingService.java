package com.redbrokers.processing.service;

import com.redbrokers.processing.communication.MarketDataCommunicator;
import com.redbrokers.processing.communication.OrderBookCommunicator;
import com.redbrokers.processing.communication.OrderCommunicator;
import com.redbrokers.processing.dto.*;
import com.redbrokers.processing.enums.Side;
import com.redbrokers.processing.enums.Status;
import com.redbrokers.processing.model.Execution;
import com.redbrokers.processing.model.Order;
import com.redbrokers.processing.repository.OrderProcessingRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private Map<String, GroupedOrder> productOrdersCacheOne = new HashMap<>();
    private Map<String, GroupedOrder> productOrdersCacheTwo = new HashMap<>();
    private List<Product> productsFromExchangeOne = new ArrayList<>();
    private List<Product> productsFromExchangeTwo = new ArrayList<>();

    @Value("${order-processing.variables.urls.exchange-one}")
    private String exchangeOneURL;

    @Value("${order-processing.variables.urls.exchange-two}")
    private String exchangeTwoURL;

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
    public void openProductsFromExchanges() {
        this.productsFromExchangeOne.addAll(marketDataCommunicator.getProductsFromExchange(exchangeOneURL));
        this.productsFromExchangeTwo.addAll(marketDataCommunicator.getProductsFromExchange(exchangeTwoURL));
    }

    //check status
    @Scheduled(initialDelay = 100, fixedDelay = 30000)
    public void checkOrderStatus() {
        List<Order> orders = this.orderProcessingRepository.getOrdersByStatus("NOT_EXECUTED");
        List<Order> partiallyExedOrders = this.orderProcessingRepository.getOrdersByStatus("PARTIALLY_EXECUTED");
        orders.addAll(partiallyExedOrders);
        orders.forEach(order -> {
                    try {
                        ResponseEntity<SingleOrder> response = (ResponseEntity<SingleOrder>) communicator.getOrderByIdRequest(order.getOrderIdFromExchange());
                        SingleOrder orderResp = response.getBody();
                        if ((!orderResp.getCumulatitiveQuantity().equals(orderResp.getQuantity())
                                &&
                                !orderResp.getCumulatitiveQuantity().equals(0))) {
                            order.setStatus(Status.PARTIALLY_EXECUTED.name());
                            this.orderProcessingRepository.save(order);
                        } else {
                            order.setStatus(Status.NOT_EXECUTED.name());
                            this.orderProcessingRepository.save(order);
                        }
                    } catch (HttpClientErrorException e) {
                        System.out.println(e.getMessage());
                    } catch (HttpServerErrorException e) {
                        order.setStatus(Status.FULLY_EXECUTED.name());
                        this.orderProcessingRepository.save(order);
                    }
                }

        );
    }
    //do multileg


    @Scheduled(initialDelay = 100, fixedDelay = 1000)
    public void populateCaches() {
        this.populateOrderBookOneCache(exchangeOneURL);
        this.populateOrderBookTwoCache(exchangeTwoURL);
    }

    public void populateOrderBookOneCache(String url) {
        System.out.println("populating order book one cache");
        for (Product product : this.productsFromExchangeOne) {
            this.getCacheData(product.getTicker(), url, this.productOrdersCacheOne);

        }
    }

    public void populateOrderBookTwoCache(String url) {
        System.out.println("populating order book two cache");
        for (Product product : this.productsFromExchangeTwo) {
            this.getCacheData(product.getTicker(), url, this.productOrdersCacheTwo);
        }
    }

    public void getCacheData(String product, String url, Map<String, GroupedOrder> cache) {
        GroupedOrder groupedOrder = new GroupedOrder();
        groupedOrder.setTicker(product);
        List<SingleOrder> ordersByTickerAndStatus = (List<SingleOrder>) getOrdersBySideAndStatus(url, "open", product);
        Map<Side, List<SingleOrder>> groupedOrdersBySide = ordersByTickerAndStatus.stream().collect(groupingBy(SingleOrder::getSide));
        groupedOrder.setSingleOrdersBuy(groupedOrdersBySide.get(Side.BUY));
        groupedOrder.setSingleOrdersSell(groupedOrdersBySide.get(Side.SELL));
        cache.put(product, groupedOrder);
    }


    public ComputedPriceAndQuantityFromExchange computeMaxBidPriceForProduct(String product) {
        OptionalDouble maxPrice1 = this.productOrdersCacheOne.get(product).getSingleOrdersBuy().stream().mapToDouble(SingleOrder::getPrice).max();
        OptionalDouble maxPrice2 = this.productOrdersCacheTwo.get(product).getSingleOrdersBuy().stream().mapToDouble(SingleOrder::getPrice).max();
        if (maxPrice1.isPresent() && maxPrice2.isPresent()) {
            ComputedPriceAndQuantityFromExchange computedPriceFromExchange;
            if (maxPrice2.getAsDouble() > maxPrice1.getAsDouble()) {
                computedPriceFromExchange = new ComputedPriceAndQuantityFromExchange();
                computedPriceFromExchange.setExchangeOneURL(exchangeTwoURL);
                computedPriceFromExchange.setSelectedPrice(maxPrice2.getAsDouble());
                computedPriceFromExchange.setSide(Side.BUY);
                int rawQuantity = this.productOrdersCacheTwo.get(product).getSingleOrdersBuy().stream().mapToInt(SingleOrder::getQuantity).sum();
                int cumulatitiveQuantity = this.productOrdersCacheTwo.get(product).getSingleOrdersBuy().stream().mapToInt(SingleOrder::getCumulatitiveQuantity).sum();
                computedPriceFromExchange.setQuantity(rawQuantity-cumulatitiveQuantity);
            } else {
                computedPriceFromExchange = new ComputedPriceAndQuantityFromExchange();
                computedPriceFromExchange.setExchangeOneURL(exchangeOneURL);
                computedPriceFromExchange.setSelectedPrice(maxPrice1.getAsDouble());
                computedPriceFromExchange.setSide(Side.BUY);
                int rawQuantity = this.productOrdersCacheOne.get(product).getSingleOrdersBuy().stream().mapToInt(SingleOrder::getQuantity).sum();
                int cumulatitiveQuantity = this.productOrdersCacheOne.get(product).getSingleOrdersBuy().stream().mapToInt(SingleOrder::getCumulatitiveQuantity).sum();
                computedPriceFromExchange.setQuantity(rawQuantity-cumulatitiveQuantity);
            }
            return computedPriceFromExchange;
        }
        return null;
    }

    public ComputedPriceAndQuantityFromExchange computeMinAskPriceForProduct(String product) {
        OptionalDouble minPrice1 = this.productOrdersCacheOne.get(product).getSingleOrdersSell().stream().mapToDouble(SingleOrder::getPrice).min();
        OptionalDouble minPrice2 = this.productOrdersCacheTwo.get(product).getSingleOrdersSell().stream().mapToDouble(SingleOrder::getPrice).min();
        if (minPrice1.isPresent() && minPrice2.isPresent()) {
            ComputedPriceAndQuantityFromExchange computedPriceFromExchange;
            if (minPrice2.getAsDouble() < minPrice1.getAsDouble()) {
                computedPriceFromExchange = new ComputedPriceAndQuantityFromExchange();
                computedPriceFromExchange.setExchangeOneURL(exchangeTwoURL);
                computedPriceFromExchange.setSelectedPrice(minPrice2.getAsDouble());
                computedPriceFromExchange.setSide(Side.SELL);
                int rawQuantity = this.productOrdersCacheTwo.get(product).getSingleOrdersSell().stream().mapToInt(SingleOrder::getQuantity).sum();
                int cumulatitiveQuantity = this.productOrdersCacheTwo.get(product).getSingleOrdersSell().stream().mapToInt(SingleOrder::getCumulatitiveQuantity).sum();
                computedPriceFromExchange.setQuantity(rawQuantity-cumulatitiveQuantity);
            } else {
                computedPriceFromExchange = new ComputedPriceAndQuantityFromExchange();
                computedPriceFromExchange.setExchangeOneURL(exchangeOneURL);
                computedPriceFromExchange.setSelectedPrice(minPrice1.getAsDouble());
                computedPriceFromExchange.setSide(Side.SELL);
                int rawQuantity = this.productOrdersCacheOne.get(product).getSingleOrdersSell().stream().mapToInt(SingleOrder::getQuantity).sum();
                int cumulatitiveQuantity = this.productOrdersCacheOne.get(product).getSingleOrdersSell().stream().mapToInt(SingleOrder::getCumulatitiveQuantity).sum();
                computedPriceFromExchange.setQuantity(rawQuantity-cumulatitiveQuantity);
            }
            return computedPriceFromExchange;
        }
        return null;
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
        ComputedPriceAndQuantityFromExchange priceAndQuantityFromExchange;
        if (orderRequestBody.getSide() == Side.SELL) {
            priceAndQuantityFromExchange = computeMaxBidPriceForProduct(orderRequestBody.getProduct());
            //            System.out.println(computeMinBidPriceForProduct(order.getProduct()));
        } else {
            priceAndQuantityFromExchange = computeMinAskPriceForProduct(order.getProduct());
            //            System.out.println(computeMinAskPriceForProduct(order.getProduct()));
        }
        order.setPrice(priceAndQuantityFromExchange.getSelectedPrice());
        order.setExchange(priceAndQuantityFromExchange.getExchangeOneURL());
        order.setStatus(Status.NOT_EXECUTED.name());
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
