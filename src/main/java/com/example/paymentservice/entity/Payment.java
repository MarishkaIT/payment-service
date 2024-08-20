package com.example.paymentservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;
    @Column(precision = 19, scale = 2)
    private BigDecimal amount;
    @Column(length = 3)
    private String currency;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
