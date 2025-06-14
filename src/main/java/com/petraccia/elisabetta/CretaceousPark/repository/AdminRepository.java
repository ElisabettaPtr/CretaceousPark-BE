package com.petraccia.elisabetta.CretaceousPark.repository;

import com.petraccia.elisabetta.CretaceousPark.model.Admin;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByAuthUser(AuthUser authUser);
}
