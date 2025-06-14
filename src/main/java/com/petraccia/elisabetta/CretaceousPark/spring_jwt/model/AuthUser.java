package com.petraccia.elisabetta.CretaceousPark.spring_jwt.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petraccia.elisabetta.CretaceousPark.model.Admin;
import com.petraccia.elisabetta.CretaceousPark.model.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email") 
    })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @NotNull
  private LocalDate registrationDate;

  @OneToOne(mappedBy = "authUser")
  @JsonManagedReference(value = "customer-authUser")
  private Customer customer;

  @OneToOne(mappedBy = "authUser")
  @JsonManagedReference(value = "admin-authUser")
  private Admin admin;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(  name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public AuthUser(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.registrationDate = LocalDate.now();
  }

  public AuthUser(String username, String email, String password, Role role) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.roles.add(role);
    this.registrationDate = LocalDate.now();
  }
}
