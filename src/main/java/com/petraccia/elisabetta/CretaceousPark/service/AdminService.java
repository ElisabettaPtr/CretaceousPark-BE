package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ConflictException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.model.Admin;
import com.petraccia.elisabetta.CretaceousPark.repository.AdminRepository;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.repository.AuthUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    private final AuthUserRepository authUserRepository;

    @Autowired
    public AdminService (AdminRepository adminRepository, AuthUserRepository authUserRepository){
        this.adminRepository = adminRepository;
        this.authUserRepository = authUserRepository;
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(Long id) {

        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        return adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

    }

    public Admin saveAdmin(Admin admin) {

        if (admin.getAuthUser() == null) {
            throw new BadRequestException("AuthUser must not be null.");
        }

        Long authUserId = admin.getAuthUser().getId();
        if (authUserId == null || !authUserRepository.existsById(authUserId)) {
            throw new ResourceNotFoundException("AuthUser not found with id: " + authUserId);
        }

        if (adminRepository.existsByAuthUser(admin.getAuthUser())) {
            throw new ConflictException("This AuthUser is already assigned to another Admin/Customer.");
        }

        return adminRepository.save(admin);

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

    public Admin updateAdmin(Long id, Admin admin) {

        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        if (admin == null) {
            throw new BadRequestException("Admin data must not be null.");
        }

        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

        existingAdmin.setFullName(admin.getFullName());
        existingAdmin.setAuthUser(admin.getAuthUser());

        return adminRepository.save(existingAdmin);

    }

    // TODO : MODIFY -> adapt to DTO

}
