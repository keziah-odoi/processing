package com.redbrokers.processing.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class OrderBookExecutions {

    private String timestamp;
    private BigDecimal price;
    private Integer quantity;

    @Override
    public String toString() {
        return "\ntimestamp= '" + timestamp + '\'' +
                ", price= " + price +
                ", quantity= " + quantity;
    }
}
