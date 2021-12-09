//package com.redbrokers.processing.model;
//
//import com.redbrokers.processing.enums.Side;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.math.BigDecimal;
//import java.util.UUID;
//
//@Entity
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class Leg {
//    @Id
//    @Column(name = "id", nullable = false)
//    private UUID id;
//    private UUID clientId;
//    private String exchangeId;
//    private String product;
//    private int quantity;
//    private BigDecimal price;
//    private Side side;
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "order_id", nullable = false)
//    private Order order;
//
//}
