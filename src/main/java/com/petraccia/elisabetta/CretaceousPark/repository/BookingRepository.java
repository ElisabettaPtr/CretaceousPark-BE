package com.petraccia.elisabetta.CretaceousPark.repository;

import com.petraccia.elisabetta.CretaceousPark.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByPlannerId(Long plannerId);

    // Trova tutti i booking i cui planner appartengono a customer con authUserId dato
    @Query("SELECT b FROM Booking b WHERE b.planner.customer.authUser.id = :authUserId")
    List<Booking> findByCustomerAuthUserId(@Param("authUserId") Long authUserId);

}
