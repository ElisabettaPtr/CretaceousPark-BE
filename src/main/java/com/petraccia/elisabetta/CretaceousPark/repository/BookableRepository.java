package com.petraccia.elisabetta.CretaceousPark.repository;

import com.petraccia.elisabetta.CretaceousPark.model.Bookable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookableRepository extends JpaRepository<Bookable, Long> {
}
