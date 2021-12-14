package com.redbrokers.processing.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class GroupedOrder {
    private String ticker;
    private List<SingleOrder> singleOrdersBuy;
    private List<SingleOrder> singleOrdersSell;
}
