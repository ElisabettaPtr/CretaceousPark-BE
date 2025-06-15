package com.petraccia.elisabetta.CretaceousPark.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private String attraction;

    @Column(nullable = false)
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

}
