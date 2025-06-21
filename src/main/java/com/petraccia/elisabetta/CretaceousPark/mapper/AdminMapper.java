package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.AdminDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Admin;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;

public class AdminMapper {

    public static AdminDTO toDTO(Admin admin) {
        if (admin == null) {
            return null;
        }

        return AdminDTO.builder()
                .id(admin.getId())
                .fullName(admin.getFullName())
                .authUserId(admin.getAuthUser() != null ? admin.getAuthUser().getId() : null)
                .build();
    }

    public static Admin toEntity(AdminDTO dto, AuthUser authUser) {
        if (dto == null) {
            return null;
        }

        return Admin.builder()
                .id(dto.getId())
                .fullName(dto.getFullName())
                .authUser(authUser)
                .build();
    }
}

