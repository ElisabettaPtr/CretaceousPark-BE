package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.AttractionDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Attraction;
import com.petraccia.elisabetta.CretaceousPark.model.Zone;

public class AttractionMapper {

    public static AttractionDTO toDTO(Attraction entity) {
        if (entity == null) return null;

        return AttractionDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .dangerLevel(entity.getDangerLevel())
                .zoneId(entity.getZone() != null ? entity.getZone().getId() : null)
                .build();
    }

    public static Attraction toEntity(AttractionDTO dto, Zone zone) {
        if (dto == null) return null;

        return Attraction.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .dangerLevel(dto.getDangerLevel())
                .zone(zone)
                .build();
    }
}



