package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.AdminDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Admin;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;

public class AdminMapper {

    public static AdminDTO toDTO(Admin admin) {
        if (admin == null) {
            return null;
        }
        Long authUserId = (admin.getAuthUser() != null) ? admin.getAuthUser().getId() : null;
        return new AdminDTO(admin.getId(), admin.getFullName(), authUserId);
    }

    public static Admin toEntity(AdminDTO dto, AuthUser authUser) {
        if (dto == null) {
            return null;
        }
        Admin admin = new Admin();
        admin.setId(dto.getId());
        admin.setFullName(dto.getFullName());
        admin.setAuthUser(authUser);  // qui passi l'entità AuthUser completa
        return admin;
    }

}
