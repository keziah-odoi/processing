package com.redbrokers.processing.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.redbrokers.processing.enums.Side;
import com.redbrokers.processing.model.Execution;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SingleOrder {

    private String product;
    private BigDecimal price;
    private Integer quantity;
    private Side side;
    private Integer cumulatitiveQuantity;
    private List<OrderBookExecutions> executions;


    @Override
    public String toString() {
        return "Single Order for " + product + ":"+ '\'' +
                ", price: " + price +
                ", quantity: " + quantity +
                ", side/status: " + side +
                ", cumulative quantity: " + cumulatitiveQuantity +
                ", executions: " + executions +
                '}'+"\n";
    }


}
