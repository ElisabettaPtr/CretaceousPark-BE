package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.PlannerDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Customer;
import com.petraccia.elisabetta.CretaceousPark.model.DayType;
import com.petraccia.elisabetta.CretaceousPark.model.Planner;

import java.util.stream.Collectors;

public class PlannerMapper {

    public static PlannerDTO toDTO(Planner entity) {
        if (entity == null) return null;

        return PlannerDTO.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .dayTypeId(entity.getDayType() != null ? entity.getDayType().getId() : null)
                .customerId(entity.getCustomer() != null ? entity.getCustomer().getId() : null)
                .ticketIds(entity.getTickets() != null ? entity.getTickets().stream()
                        .map(t -> t.getId())
                        .collect(Collectors.toList()) : null)
                .bookingIds(entity.getBookings() != null ? entity.getBookings().stream()
                        .map(b -> b.getId())
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public static Planner toEntity(PlannerDTO dto, DayType dayType, Customer customer) {
        if (dto == null) return null;
        return Planner.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .dayType(dayType)
                .customer(customer)
                .build();
    }
}

