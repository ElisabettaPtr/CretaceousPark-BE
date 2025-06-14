package com.petraccia.elisabetta.CretaceousPark.repository;

import com.petraccia.elisabetta.CretaceousPark.model.NonBookable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NonBookableRepository extends JpaRepository<NonBookable, Long> {
}
