package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.BookableDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Bookable;
import com.petraccia.elisabetta.CretaceousPark.model.TypeService;

public class BookableMapper {

    public static BookableDTO toDTO(Bookable entity) {
        if (entity == null) return null;
        return BookableDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .typeServiceId(entity.getTypeService() != null ? entity.getTypeService().getId() : null)
                .build();
    }

    public static Bookable toEntity(BookableDTO dto, TypeService typeService) {
        if (dto == null) return null;
        return Bookable.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .typeService(typeService)
                .build();
    }
}

