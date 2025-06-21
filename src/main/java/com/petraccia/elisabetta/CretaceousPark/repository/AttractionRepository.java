package com.petraccia.elisabetta.CretaceousPark.repository;

import com.petraccia.elisabetta.CretaceousPark.model.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {

    List<Attraction> findByZoneId(Long zoneId);

    Optional<Attraction> findByName(String name);

}
