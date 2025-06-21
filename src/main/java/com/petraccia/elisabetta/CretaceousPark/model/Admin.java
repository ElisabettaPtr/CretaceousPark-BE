package com.petraccia.elisabetta.CretaceousPark.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @OneToOne
    @JsonBackReference(value = "admin-authUser")
    private AuthUser authUser;

}
