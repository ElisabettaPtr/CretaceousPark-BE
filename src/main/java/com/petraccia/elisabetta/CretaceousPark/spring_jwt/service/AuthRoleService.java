package com.petraccia.elisabetta.CretaceousPark.spring_jwt.service;

import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.ERole;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.Role;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.repository.AuthRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthRoleService {

    private final AuthRoleRepository authRoleRepository;

    @Autowired
    public AuthRoleService(AuthRoleRepository authRoleRepository) {
        this.authRoleRepository = authRoleRepository;
    }

    public List<Role> getAllRoles() {
        return authRoleRepository.findAll();
    }

    public Role findByName(ERole name) {
        return authRoleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + name));
    }

    public Role saveRole(Role role) {
        return authRoleRepository.save(role);
    }

    public void deleteRoleById(Long id) {
        if (authRoleRepository.existsById(id)) {
            authRoleRepository.deleteById(id);
        } else {
            throw new RuntimeException("Role not found with id: " + id);
        }
    }

    public Role getRoleById(Long id) {
        return authRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

}

