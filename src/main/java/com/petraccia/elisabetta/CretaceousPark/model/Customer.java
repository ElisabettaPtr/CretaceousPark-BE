package com.petraccia.elisabetta.CretaceousPark.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @NotNull
    private LocalDate birthdate;

    @OneToOne(mappedBy = "customer")
    @JsonBackReference(value = "customer-wallet")
    private Wallet wallet;

    // TODO : FOREIGN KEYS

}
