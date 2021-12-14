package com.redbrokers.processing.dto;

import com.redbrokers.processing.enums.Side;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
@NoArgsConstructor
@Getter
@Setter
public class ComputedPriceFromExchange {
    private String exchangeOneURL;
    private Double selectedPrice;
    private Side side;
}
