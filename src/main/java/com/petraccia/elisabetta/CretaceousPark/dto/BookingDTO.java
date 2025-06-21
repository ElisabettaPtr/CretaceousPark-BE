package com.petraccia.elisabetta.CretaceousPark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private LocalDate date;
    private int reservationQty;
    private Long restaurantId;
    private Long bookableId;
    private Long plannerId;
}
