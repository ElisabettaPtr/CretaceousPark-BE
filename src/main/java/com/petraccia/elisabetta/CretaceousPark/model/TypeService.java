package com.petraccia.elisabetta.CretaceousPark.model;

import com.petraccia.elisabetta.CretaceousPark.enums.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private AvailabilityStatus type;

    @OneToMany(mappedBy = "typeService")
    private List<Bookable> bookableServices;

    @OneToMany(mappedBy = "typeService")
    private List<NonBookable> nonBookableServices;

}
