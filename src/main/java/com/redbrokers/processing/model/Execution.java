package com.redbrokers.processing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Execution {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    private String timestamp;
    private Double price;
    private Integer quantity;
    private String exchange;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;


}
