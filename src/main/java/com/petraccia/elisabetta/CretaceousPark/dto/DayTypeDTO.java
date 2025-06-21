package com.petraccia.elisabetta.CretaceousPark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayTypeDTO {
    private Long id;
    private String name;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean isOpen;
}
