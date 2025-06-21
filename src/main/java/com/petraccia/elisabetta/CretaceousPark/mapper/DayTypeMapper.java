package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.DayTypeDTO;
import com.petraccia.elisabetta.CretaceousPark.model.DayType;

public class DayTypeMapper {

    public static DayTypeDTO toDTO(DayType entity) {
        if (entity == null) return null;
        return DayTypeDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .openTime(entity.getOpenTime())
                .closeTime(entity.getCloseTime())
                .isOpen(entity.isOpen())
                .build();
    }

    public static DayType toEntity(DayTypeDTO dto) {
        if (dto == null) return null;
        return new DayType(dto.getId(), dto.getName(), dto.getOpenTime(), dto.getCloseTime(), dto.isOpen());
    }
}

