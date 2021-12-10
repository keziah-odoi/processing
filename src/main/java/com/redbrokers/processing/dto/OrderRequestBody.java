package com.redbrokers.processing.dto;

import com.redbrokers.processing.enums.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestBody {
    private String product;
    private Integer quantity;
    private Double price;
    private Side side;



    @Override
    public String toString() {
        return "OrderRequestBody{" +
                "product='" + product + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", side=" + side +
                '}';
    }
}
