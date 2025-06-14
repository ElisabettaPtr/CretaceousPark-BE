package com.petraccia.elisabetta.CretaceousPark.spring_jwt.controller;

import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.ERole;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.Role;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.payload.request.LoginRequest;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.payload.request.SignupRequest;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.payload.response.JwtResponse;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.payload.response.MessageResponse;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.service.AuthRoleService;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.service.AuthUserService;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.util.security.jwt.JwtUtils;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.util.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Controller per la gestione dell'autenticazione e della registrazione.
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  AuthUserService authUserService;

  @Autowired
  AuthRoleService authRoleService;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  /**
   * Login
   */
  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
  }

  /**
   * Registrazione
   */
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

    if (authUserService.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (authUserService.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    AuthUser user = new AuthUser(
            signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword())
    );

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      roles.add(authRoleService.findByName(ERole.ROLE_CUSTOMER));
    } else {
      strRoles.forEach(role -> {
          if (role.toLowerCase().equals("admin")) {
              roles.add(authRoleService.findByName(ERole.ROLE_ADMIN));
          } else {
              roles.add(authRoleService.findByName(ERole.ROLE_CUSTOMER));
          }
      });
    }

    user.setRoles(roles);
    authUserService.saveUser(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}

