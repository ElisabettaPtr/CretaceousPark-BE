package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.RestaurantDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Booking;
import com.petraccia.elisabetta.CretaceousPark.model.Restaurant;

import java.util.stream.Collectors;

import java.util.List;

public class RestaurantMapper {

    public static RestaurantDTO toDTO(Restaurant entity) {
        if (entity == null) return null;

        List<Long> bookingIds = null;
        if (entity.getBookings() != null) {
            bookingIds = entity.getBookings().stream()
                    .map(Booking::getId)
                    .collect(Collectors.toList());
        }

        return RestaurantDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .kitchenType(entity.getKitchenType())
                .bookingIds(bookingIds)
                .build();
    }

    public static Restaurant toEntity(RestaurantDTO dto, List<Booking> bookings) {
        if (dto == null) return null;

        return Restaurant.builder()
                .id(dto.getId())
                .name(dto.getName())
                .kitchenType(dto.getKitchenType())
                .bookings(bookings)
                .build();
    }
}

