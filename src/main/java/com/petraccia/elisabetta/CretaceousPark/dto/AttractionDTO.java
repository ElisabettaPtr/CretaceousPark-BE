package com.petraccia.elisabetta.CretaceousPark.dto;

import com.petraccia.elisabetta.CretaceousPark.enums.DangerLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttractionDTO {
    private Long id;
    private String name;
    private String description;
    private DangerLevel dangerLevel;
    private Long zoneId;
}
