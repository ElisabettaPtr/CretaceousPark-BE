package com.petraccia.elisabetta.CretaceousPark.repository;

import com.petraccia.elisabetta.CretaceousPark.model.Customer;
import com.petraccia.elisabetta.CretaceousPark.model.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlannerRepository extends JpaRepository<Planner, Long> {

    Optional<Planner> findByCustomer(Customer customer);

    // Trova il planner associato al customer con authUserId dato
    @Query("SELECT p FROM Planner p WHERE p.customer.authUser.id = :authUserId")
    Optional<Planner> findByCustomerAuthUserId(@Param("authUserId") Long authUserId);

    boolean existsByIdAndCustomerAuthUserId(Long plannerId, Long authUserId);

    List<Planner> findAllByCustomer(Customer customer);


}
