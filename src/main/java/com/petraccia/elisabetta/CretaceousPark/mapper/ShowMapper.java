package com.petraccia.elisabetta.CretaceousPark.mapper;


import com.petraccia.elisabetta.CretaceousPark.dto.ShowDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Show;
import com.petraccia.elisabetta.CretaceousPark.model.Zone;

public class ShowMapper {

    public static ShowDTO toDTO(Show entity) {
        if (entity == null) return null;

        return ShowDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .attraction(entity.getAttraction())
                .time(entity.getTime())
                .zoneId(entity.getZone() != null ? entity.getZone().getId() : null)
                .build();
    }

    public static Show toEntity(ShowDTO dto, Zone zone) {
        if (dto == null) return null;

        return Show.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .attraction(dto.getAttraction())
                .time(dto.getTime())
                .zone(zone)
                .build();
    }
}


