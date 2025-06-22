package com.petraccia.elisabetta.CretaceousPark.repository;

import com.petraccia.elisabetta.CretaceousPark.model.NonBookable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NonBookableRepository extends JpaRepository<NonBookable, Long> {

    Optional<NonBookable> findByName(String name);

}
