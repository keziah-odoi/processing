package com.redbrokers.processing.model;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Execution {
    private String timestamp;
    private BigDecimal price;
    private Integer quantity;
}
