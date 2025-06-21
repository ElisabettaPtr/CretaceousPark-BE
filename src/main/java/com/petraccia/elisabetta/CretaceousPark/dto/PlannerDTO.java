package com.petraccia.elisabetta.CretaceousPark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannerDTO {
    private Long id;
    private LocalDate date;
    private Long dayTypeId;
    private Long customerId;
    private List<Long> ticketIds;
    private List<Long> bookingIds;
}
