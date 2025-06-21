package com.petraccia.elisabetta.CretaceousPark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowDTO {
    private Long id;
    private String name;
    private String description;
    private String attraction;
    private LocalTime time;
    private Long zoneId;
}
