package com.petraccia.elisabetta.CretaceousPark.spring_jwt.service;

import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthUserService {

    private final AuthUserRepository authUserRepository;

    @Autowired
    public AuthUserService(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    public List<AuthUser> getAllUsers() {
        return authUserRepository.findAll();
    }

    public AuthUser getUserById(Long id) {
        return authUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public AuthUser findByUsername(String username) {
        return authUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public boolean existsByUsername(String username) {
        return authUserRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return authUserRepository.existsByEmail(email);
    }

    public AuthUser saveUser(AuthUser user) {
        return authUserRepository.save(user);
    }

    public void deleteUserById(Long id) {
        if (authUserRepository.existsById(id)) {
            authUserRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

}

