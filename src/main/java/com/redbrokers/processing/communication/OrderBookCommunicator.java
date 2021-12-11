package com.redbrokers.processing.communication;

import com.redbrokers.processing.dto.FullOrderBook;
import com.redbrokers.processing.dto.SingleOrder;
import com.redbrokers.processing.exceptions.EntityNotFoundException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderBookCommunicator {
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookCommunicator.class);

    public Iterable<FullOrderBook> getOrderBooks(String exchangeURL) throws EntityNotFoundException {
        String uri = exchangeURL + "/orderbook";
        LOGGER.info("orderbook" + uri);
        FullOrderBook[] fullOrderBooks = restTemplate.getForObject(uri, FullOrderBook[].class);
        if (fullOrderBooks != null) {
            return Arrays.asList(fullOrderBooks);
        } else {
            throw new EntityNotFoundException("No orderbook found.");
        }
    }

    public Iterable<SingleOrder> getOrdersBySideOrStatus(String exchangeURL, String product, String sideOrStatus) {

        String uri = exchangeURL + "/orderbook/" + product + "/" + sideOrStatus;
        LOGGER.info(sideOrStatus + " " + uri);
        try {
            SingleOrder[] singleOrders = restTemplate.getForObject(uri, SingleOrder[].class);
            if (singleOrders != null) {
                return Arrays.asList(singleOrders);
            }
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
        } catch (HttpServerErrorException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }
}


