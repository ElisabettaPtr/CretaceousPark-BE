package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.ZoneDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Attraction;
import com.petraccia.elisabetta.CretaceousPark.model.Show;
import com.petraccia.elisabetta.CretaceousPark.model.Zone;

import java.util.stream.Collectors;

import java.util.List;

public class ZoneMapper {

    public static ZoneDTO toDTO(Zone entity) {
        if (entity == null) return null;

        return ZoneDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .attractions(entity.getAttractions() != null
                        ? entity.getAttractions().stream()
                        .map(AttractionMapper::toDTO)
                        .collect(Collectors.toList())
                        : null)
                .shows(entity.getShows() != null
                        ? entity.getShows().stream()
                        .map(ShowMapper::toDTO)
                        .collect(Collectors.toList())
                        : null)
                .build();
    }

    public static Zone toEntity(ZoneDTO dto, List<Attraction> attractions, List<Show> shows) {
        if (dto == null) return null;

        return Zone.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .attractions(attractions)
                .shows(shows)
                .build();
    }
}



