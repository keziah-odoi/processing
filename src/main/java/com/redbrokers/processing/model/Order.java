package com.redbrokers.processing.model;
import com.redbrokers.processing.enums.Side;
import com.redbrokers.processing.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="kez")
public class Order {
    @Id
    @Column(name = "id", nullable = false)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID clientId;
    private String product;
    private int quantity;
    @Column(name = "price",length=8,precision=2)
    private Double price;
    private String side;
    private LocalDateTime time;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private List<Execution> executions;
    private String orderIdFromExchange;
    private String exchange;
    private String status;


    @PrePersist
    public void prePersist(){
        time = LocalDateTime.now();
        id = UUID.randomUUID();
    }




}
