package com.redbrokers.processing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
public class Execution {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String timestamp;
    private BigDecimal price;
    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;


}
