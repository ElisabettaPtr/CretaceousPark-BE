package com.petraccia.elisabetta.CretaceousPark.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false)
    private LocalTime createdAt;

    @Column
    private LocalTime updatedAt;

    @OneToOne
    @JsonBackReference(value = "customer-wallet")
    private Customer customer;

}
