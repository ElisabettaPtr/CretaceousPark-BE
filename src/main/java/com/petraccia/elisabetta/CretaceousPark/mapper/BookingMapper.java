package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.BookingDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Bookable;
import com.petraccia.elisabetta.CretaceousPark.model.Booking;
import com.petraccia.elisabetta.CretaceousPark.model.Planner;
import com.petraccia.elisabetta.CretaceousPark.model.Restaurant;

public class BookingMapper {
    public static BookingDTO toDTO(Booking entity) {
        if (entity == null) return null;
        return BookingDTO.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .reservationQty(entity.getReservationQty())
                .restaurantId(entity.getRestaurant() != null ? entity.getRestaurant().getId() : null)
                .bookableId(entity.getBookable() != null ? entity.getBookable().getId() : null)
                .plannerId(entity.getPlanner() != null ? entity.getPlanner().getId() : null)
                .build();
    }

    public static Booking toEntity(BookingDTO dto, Restaurant restaurant, Bookable bookable, Planner planner) {
        if (dto == null) return null;

        if (restaurant != null && bookable != null) {
            throw new IllegalArgumentException("A booking can be linked to either a Restaurant or a Bookable, not both.");
        }

        return Booking.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .reservationQty(dto.getReservationQty())
                .restaurant(restaurant)
                .bookable(bookable)
                .planner(planner)
                .build();
    }
}

