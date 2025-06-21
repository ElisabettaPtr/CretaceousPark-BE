package com.petraccia.elisabetta.CretaceousPark.repository;

import com.petraccia.elisabetta.CretaceousPark.model.Bookable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookableRepository extends JpaRepository<Bookable, Long> {

    Optional<Bookable> findByName(String name);

}
