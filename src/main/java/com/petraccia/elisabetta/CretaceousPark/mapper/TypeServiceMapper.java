package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.TypeServiceDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Bookable;
import com.petraccia.elisabetta.CretaceousPark.model.NonBookable;
import com.petraccia.elisabetta.CretaceousPark.model.TypeService;

import java.util.stream.Collectors;

public class TypeServiceMapper {

    public static TypeServiceDTO toDTO(TypeService entity) {
        if (entity == null) return null;

        return TypeServiceDTO.builder()
                .id(entity.getId())
                .type(entity.getType())
                .bookableServiceIds(entity.getBookableServices() != null
                        ? entity.getBookableServices().stream().map(Bookable::getId).collect(Collectors.toList())
                        : null)
                .nonBookableServiceIds(entity.getNonBookableServices() != null
                        ? entity.getNonBookableServices().stream().map(NonBookable::getId).collect(Collectors.toList())
                        : null)
                .build();
    }

    public static TypeService toEntity(TypeServiceDTO dto) {
        if (dto == null) return null;

        return TypeService.builder()
                .id(dto.getId())
                .type(dto.getType())
                .build();
    }
}
