package com.redbrokers.processing.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

        @JsonAlias({"TICKER", "ticker"})
        private String ticker;

        @JsonAlias({"SELL_LIMIT", "sellLimit"})
        private Integer sellLimit;

        @JsonAlias({"MAX_PRICE_SHIFT", "maxPriceShift"})
        private Double maxPriceShift;

        @JsonAlias({"ASK_PRICE", "askPrice"})
        private Double askPrice;

        @JsonAlias({"BID_PRICE", "bidPrice"})
        private Double bidPrice;

        @JsonAlias({"BUY_LIMIT", "buyLimit"})
        private Integer buyLimit;

        @JsonAlias({"LAST_TRADED_PRICE", "lastTradedPrice"})
        private Double lastTradedPrice;


        public Product() {
        }

        public Product(String ticker, Integer sellLimit, Double maxPriceShift, Double askPrice, Double bidPrice, Integer buyLimit, Double lastTradedPrice) {
                this.ticker = ticker;
                this.sellLimit = sellLimit;
                this.maxPriceShift = maxPriceShift;
                this.askPrice = askPrice;
                this.bidPrice = bidPrice;
                this.buyLimit = buyLimit;
                this.lastTradedPrice = lastTradedPrice;
        }

        @Override
        public String toString() {
            return "Products from exchange : " +

                    ", ticker='" + ticker + '\'' +
                    ", sellLimit=" + sellLimit +
                    ", maxPriceShift=" + maxPriceShift +
                    ", askPrice=" + askPrice +
                    ", bidPrice=" + bidPrice +
                    ", buyLimit=" + buyLimit +
                    '}' + '\n';
        }
    }

