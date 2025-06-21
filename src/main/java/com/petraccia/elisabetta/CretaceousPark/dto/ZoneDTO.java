package com.petraccia.elisabetta.CretaceousPark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZoneDTO {
    private Long id;
    private String name;
    private String description;
    private List<AttractionDTO> attractions;
    private List<ShowDTO> shows;
}
