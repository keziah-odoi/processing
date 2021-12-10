package com.redbrokers.processing.communication;

import com.redbrokers.processing.dto.FullOrderBook;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class OrderBookCommunicator {
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookCommunicator.class);

    public static Iterable<FullOrderBook> getOrderBooks(String exchangeURL) {
        String uri = exchangeURL + "/orderbook";
        LOGGER.info("orderbook" + uri);
        Iterable<FullOrderBook> fullOrderBook = null;
        try {
            FullOrderBook[] fullOrderBooks = restTemplate.getForObject(uri, FullOrderBook[].class);
            if (fullOrderBooks != null) {
                return Arrays.asList(fullOrderBooks);
            }
            return null;
        } catch (RestClientException e) {
            LOGGER.info("Could not read the longer book for" + uri);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return null;
    }

}
