package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.NonBookableDTO;
import com.petraccia.elisabetta.CretaceousPark.model.NonBookable;
import com.petraccia.elisabetta.CretaceousPark.model.TypeService;

public class NonBookableMapper {

    public static NonBookableDTO toDTO(NonBookable entity) {
        if (entity == null) return null;
        return NonBookableDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .typeServiceId(entity.getTypeService() != null ? entity.getTypeService().getId() : null)
                .build();
    }

    public static NonBookable toEntity(NonBookableDTO dto, TypeService typeService) {
        if (dto == null) return null;
        return new NonBookable(dto.getId(), dto.getName(), typeService);
    }
}

