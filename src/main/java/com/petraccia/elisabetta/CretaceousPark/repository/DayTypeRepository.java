package com.petraccia.elisabetta.CretaceousPark.repository;

import com.petraccia.elisabetta.CretaceousPark.model.DayType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DayTypeRepository extends JpaRepository<DayType, Long> {

    Optional<DayType> findByName(String name);

    Optional<DayType> findByNameIgnoreCase(String name);

}
