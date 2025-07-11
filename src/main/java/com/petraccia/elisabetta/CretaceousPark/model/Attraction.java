package com.petraccia.elisabetta.CretaceousPark.model;

import com.petraccia.elisabetta.CretaceousPark.enums.DangerLevel;
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
public class Attraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private DangerLevel dangerLevel;

    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

}
