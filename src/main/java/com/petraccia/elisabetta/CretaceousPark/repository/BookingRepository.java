package com.petraccia.elisabetta.CretaceousPark.repository;

import com.petraccia.elisabetta.CretaceousPark.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByPlannerId(Long plannerId);

}
