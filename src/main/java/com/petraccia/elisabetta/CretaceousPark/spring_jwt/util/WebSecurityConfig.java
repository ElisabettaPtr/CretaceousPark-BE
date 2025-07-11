package com.petraccia.elisabetta.CretaceousPark.spring_jwt.util;

import com.petraccia.elisabetta.CretaceousPark.spring_jwt.util.security.jwt.AuthEntryPointJwt;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.util.security.jwt.AuthTokenFilter;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.util.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configurazione della sicurezza dell'applicazione.
 * Definisce i provider di autenticazione, i filtri di sicurezza e le regole di accesso.
 */
@Configuration
@EnableMethodSecurity(
    prePostEnabled = true)
public class WebSecurityConfig {  
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  /**
   * Configura l'algoritmo di hashing per la gestione delle password.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configura la catena dei filtri di sicurezza, gestendo l'accesso alle risorse protette.
   */
  @SuppressWarnings("removal")
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  
      // Disabilita CSRF (necessario per H2)
      http.cors().and().csrf().disable()
  
          // Disabilita frameOptions per permettere H2 Console
          .headers().frameOptions().disable().and()
  
          // Configura la gestione delle eccezioni per richieste non autorizzate
          .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
  
          // Imposta la gestione delle sessioni come stateless (JWT non utilizza sessioni)
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
  
          // Configura le autorizzazioni sulle richieste HTTP
          .authorizeHttpRequests()
          
          // Permetti l'accesso alla console H2 senza autenticazione
          .requestMatchers("/h2-console/**").permitAll()
          
          // Permetti l'accesso senza autenticazione agli endpoint di autenticazione
          .requestMatchers("/api/auth/**").permitAll()
  
          // Permetti l'accesso senza autenticazione agli endpoint di test
          .requestMatchers("/api/test/**").permitAll()

          .requestMatchers(
            "/v3/api-docs/**",    // OpenAPI JSON
            "/swagger-ui/**",     // Swagger UI risorse
            "/swagger-ui.html",   // Swagger UI principale
            "/webjars/**"         // Risorse statiche di Swagger
            ).permitAll()
  
          // Tutte le altre richieste devono essere autenticate
          .anyRequest().permitAll();;
  
      // Configura il provider di autenticazione
      http.authenticationProvider(authenticationProvider());
  
      // Aggiunge il filtro JWT prima del filtro predefinito di autenticazione con username e password
      http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
  
      return http.build();
  }
  

}
