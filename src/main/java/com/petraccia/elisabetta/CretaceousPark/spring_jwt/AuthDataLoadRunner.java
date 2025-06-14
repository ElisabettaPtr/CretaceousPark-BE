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

        Role r_admin = new Role();
        r_admin.setName(ERole.ROLE_ADMIN);
        authRoleRepository.save(r_admin);

        Role r_customer = new Role();
        r_customer.setName(ERole.ROLE_CUSTOMER);
        authRoleRepository.save(r_customer);

        AuthUser u_admin = new AuthUser("user_admin", "adminEmail@gmail.com", encoder.encode("mygoodpassword"), r_admin);
        authUserRepository.save(u_admin);

        AuthUser u_customer = new AuthUser("user_customer", "customerEmail@gmail.com", encoder.encode("mygoodpassword"), r_customer);
        authUserRepository.save(u_customer);

    }
}   
