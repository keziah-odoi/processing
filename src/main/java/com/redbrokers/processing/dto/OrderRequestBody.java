package com.redbrokers.processing.dto;

import com.redbrokers.processing.enums.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestBody {
    private String product;
    private Integer quantity;
    private BigDecimal price;
    private Side side;
    private UUID clientId;


    @Override
    public String toString() {
        return "OrderRequestBody{" +
                "product='" + product + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", side=" + side +
                ", clientId=" + clientId +
                '}';
    }
}
