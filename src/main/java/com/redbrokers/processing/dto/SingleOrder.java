package com.redbrokers.processing.dto;

import com.redbrokers.processing.enums.Side;
import com.redbrokers.processing.model.Execution;

import java.math.BigDecimal;

public class SingleOrder {
    private String Product;
    private BigDecimal price;
    private Integer quantity;
    private Side side;
    private Integer cumulatitiveQuanity;
    private Execution executions;

}
