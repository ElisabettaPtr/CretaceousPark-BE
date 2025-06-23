package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.TicketDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Attraction;
import com.petraccia.elisabetta.CretaceousPark.model.Planner;
import com.petraccia.elisabetta.CretaceousPark.model.Show;
import com.petraccia.elisabetta.CretaceousPark.model.Ticket;

public class TicketMapper {

    public static TicketDTO toDTO(Ticket entity) {
        if (entity == null) return null;

        return TicketDTO.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .price(entity.getPrice())
                .isSold(entity.isSold())
                .attractionId(entity.getAttraction() != null ? entity.getAttraction().getId() : null)
                .showId(entity.getShow() != null ? entity.getShow().getId() : null)
                .plannerId(entity.getPlanner() != null ? entity.getPlanner().getId() : null)
                .build();
    }

    public static Ticket toEntity(TicketDTO dto, Attraction attraction, Show show, Planner planner) {
        if (dto == null) return null;

        // Se entrambi attraction e show sono passati, dai precedenza a uno oppure lancia eccezione
        if (attraction != null && show != null) {
            throw new IllegalArgumentException("Un ticket può avere solo un attraction oppure uno show, non entrambi.");
        }

        return Ticket.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .price(dto.getPrice())
                .isSold(dto.isSold())
                .attraction(attraction != null ? attraction : null)
                .show(show != null && attraction == null ? show : null)
                .planner(planner)
                .build();
    }
}
