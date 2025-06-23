package com.petraccia.elisabetta.CretaceousPark.repository;

import com.petraccia.elisabetta.CretaceousPark.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByAttractionId(Long attractionId);

    List<Ticket> findByShowId(Long showId);

    List<Ticket> findByPlannerId(Long plannerId);

}
