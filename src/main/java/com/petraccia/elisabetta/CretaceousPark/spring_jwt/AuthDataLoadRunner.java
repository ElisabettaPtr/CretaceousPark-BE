package com.petraccia.elisabetta.CretaceousPark.spring_jwt;

import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.ERole;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.Role;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.repository.AuthRoleRepository;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class AuthDataLoadRunner implements CommandLineRunner {

    @Autowired
    AuthUserRepository authUserRepository;

    @Autowired
    AuthRoleRepository authRoleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {



    }
}   
