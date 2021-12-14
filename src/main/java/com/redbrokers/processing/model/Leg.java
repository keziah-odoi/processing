package com.redbrokers.processing.model;

import com.redbrokers.processing.enums.Side;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

//@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Leg {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clientId;
    private String idFromExchange;
    private String product;
    private int quantity;
    private Double price;
    private Side side;
}
