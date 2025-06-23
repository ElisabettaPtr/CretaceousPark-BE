package com.petraccia.elisabetta.CretaceousPark.controller;

import com.petraccia.elisabetta.CretaceousPark.dto.CustomerDTO;
import com.petraccia.elisabetta.CretaceousPark.service.CustomerService;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final AuthUserService authUserService;

    @Autowired
    public CustomerController(CustomerService customerService, AuthUserService authUserService) {
        this.customerService = customerService;
        this.authUserService = authUserService;
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customerDTO = customerService.getCustomerById(id);

        if (customerDTO != null) {
            return ResponseEntity.ok(customerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/auth-user/{authUserId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerDTO> getCustomerByAuthUserId(@PathVariable Long authUserId) {
        CustomerDTO customerDTO = customerService.getCustomerByAuthUserId(authUserId);
        return ResponseEntity.ok(customerDTO);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.saveCustomer(customerDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);

        if (updatedCustomer != null) {
            return ResponseEntity.ok(updatedCustomer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /* ENDPOINT PER CUSTOMER */

    // CUSTOMER salva i propri dati (crea un nuovo customer)
    @PostMapping("/save-self")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerDTO> saveSelf(
            @RequestBody CustomerDTO customerDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        customerDTO.setAuthUserId(authUser.getId());

        // Se esiste già un customer associato, rifiuta la creazione (o potresti decidere di aggiornarlo)
        if (customerService.getCustomerByAuthUserId(authUser.getId()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null); // Oppure un messaggio di errore più dettagliato
        }

        CustomerDTO savedCustomer = customerService.saveCustomer(customerDTO);
        return ResponseEntity.ok(savedCustomer);
    }

    // CUSTOMER aggiorna i propri dati
    @PutMapping("/update-self")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerDTO> updateSelf(
            @RequestBody CustomerDTO customerDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomerDTO existingCustomer = customerService.getCustomerByAuthUserId(authUser.getId());
        if (existingCustomer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Imposta l'id e authUserId esatti per evitare modifiche non autorizzate
        customerDTO.setId(existingCustomer.getId());
        customerDTO.setAuthUserId(authUser.getId());

        CustomerDTO updatedCustomer = customerService.updateCustomer(existingCustomer.getId(), customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    // CUSTOMER cancella il proprio customer (soft delete o hard delete in base a implementazione)
    @DeleteMapping("/delete-self")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> deleteSelf(@AuthenticationPrincipal UserDetails userDetails) {

        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomerDTO existingCustomer = customerService.getCustomerByAuthUserId(authUser.getId());
        if (existingCustomer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        customerService.deleteCustomerById(existingCustomer.getId());

        return ResponseEntity.ok("Customer deleted successfully");
    }


}
