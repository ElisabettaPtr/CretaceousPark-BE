package com.petraccia.elisabetta.CretaceousPark.spring_jwt.repository;

import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.ERole;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRoleRepository extends JpaRepository<Role, Long> {
  
  Optional<Role> findByName(ERole name);
}
