package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.AdminDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ConflictException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.AdminMapper;
import com.petraccia.elisabetta.CretaceousPark.model.Admin;
import com.petraccia.elisabetta.CretaceousPark.repository.AdminRepository;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.repository.AuthUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final AuthUserRepository authUserRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository, AuthUserRepository authUserRepository) {
        this.adminRepository = adminRepository;
        this.authUserRepository = authUserRepository;
    }

    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll()
                .stream()
                .map(AdminMapper::toDTO)
                .toList();
    }

    public AdminDTO getAdminById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

        return AdminMapper.toDTO(admin);
    }

    public AdminDTO getAdminByAuthUserId(Long authUserId) {
        if (authUserId == null) {
            throw new BadRequestException("AuthUser ID must not be null.");
        }

        Admin admin = adminRepository.findByAuthUserId(authUserId);
        if (admin == null) {
            throw new ResourceNotFoundException("Admin not found with AuthUser ID: " + authUserId);
        }

        return AdminMapper.toDTO(admin);
    }

    public AdminDTO saveAdmin(AdminDTO adminDTO) {
        if (adminDTO.getAuthUserId() == null) {
            throw new BadRequestException("AuthUserId must not be null.");
        }

        AuthUser authUser = authUserRepository.findById(adminDTO.getAuthUserId())
                .orElseThrow(() -> new ResourceNotFoundException("AuthUser not found with id: " + adminDTO.getAuthUserId()));

        if (adminRepository.existsByAuthUser(authUser)) {
            throw new ConflictException("This AuthUser is already assigned to another Admin/Customer.");
        }

        Admin adminToSave = AdminMapper.toEntity(adminDTO, authUser);
        Admin savedAdmin = adminRepository.save(adminToSave);
        return AdminMapper.toDTO(savedAdmin);
    }

    @Transactional
    public void deleteAdminById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

        AuthUser user = admin.getAuthUser();
        if (user != null) {
            user.setAdmin(null);
            authUserRepository.save(user);
        }

        adminRepository.deleteById(id);
    }

    public AdminDTO updateAdmin(Long id, AdminDTO adminDTO) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        if (adminDTO == null) {
            throw new BadRequestException("Admin data must not be null.");
        }

        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

        AuthUser authUser = null;
        if (adminDTO.getAuthUserId() != null) {
            authUser = authUserRepository.findById(adminDTO.getAuthUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("AuthUser not found with id: " + adminDTO.getAuthUserId()));
        }

        existingAdmin.setFullName(adminDTO.getFullName());
        existingAdmin.setAuthUser(authUser);

        Admin updated = adminRepository.save(existingAdmin);
        return AdminMapper.toDTO(updated);
    }
}

