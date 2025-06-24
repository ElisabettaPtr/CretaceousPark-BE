package com.petraccia.elisabetta.CretaceousPark.controller;


import com.petraccia.elisabetta.CretaceousPark.dto.CustomerDTO;
import com.petraccia.elisabetta.CretaceousPark.dto.WalletDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.exception.UnauthorizedException;
import com.petraccia.elisabetta.CretaceousPark.service.CustomerService;
import com.petraccia.elisabetta.CretaceousPark.service.WalletService;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;
    private final AuthUserService authUserService;
    private final CustomerService customerService;

    @Autowired
    public WalletController(WalletService walletService, AuthUserService authUserService, CustomerService customerService) {
        this.walletService = walletService;
        this.authUserService = authUserService;
        this.customerService = customerService;
    }

    /* ENDPOINTS FOR ADMIN */

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WalletDTO>> getAllWallets() {
        return ResponseEntity.ok(walletService.getAllWallets());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WalletDTO> getWalletById(@PathVariable Long id) {
        return ResponseEntity.ok(walletService.getWalletById(id));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WalletDTO> saveWallet(@RequestBody WalletDTO walletDTO) {
        return ResponseEntity.ok(walletService.saveWallet(walletDTO));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WalletDTO> updateWallet(@PathVariable Long id, @RequestBody WalletDTO walletDTO) {
        return ResponseEntity.ok(walletService.updateWallet(id, walletDTO));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteWallet(@PathVariable Long id) {
        walletService.deleteWalletById(id);
        return ResponseEntity.ok("Wallet deleted successfully.");
    }

    /* ENDPOINTS FOR CUSTOMER */

    @GetMapping("/my-wallet")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<WalletDTO> getMyWallet(@AuthenticationPrincipal UserDetails userDetails) {
        Long authUserId = getAuthUserId(userDetails);
        Long customerId = getCustomerId(authUserId);
        return ResponseEntity.ok(walletService.getWalletByCustomerId(customerId));
    }

    @PostMapping("/save-my-wallet")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<WalletDTO> saveMyWallet(
            @RequestBody WalletDTO walletDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long authUserId = getAuthUserId(userDetails);
        Long customerId = getCustomerId(authUserId);

        walletDTO.setCustomerId(customerId);

        return ResponseEntity.ok(walletService.saveWallet(walletDTO));
    }

    @PutMapping("/update-my-wallet")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<WalletDTO> updateMyWallet(
            @RequestBody WalletDTO walletDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long authUserId = getAuthUserId(userDetails);
        Long customerId = getCustomerId(authUserId);

        WalletDTO existingWallet = walletService.getWalletByCustomerId(customerId);
        walletDTO.setCustomerId(customerId);

        return ResponseEntity.ok(walletService.updateWallet(existingWallet.getId(), walletDTO));
    }

    @DeleteMapping("/delete-my-wallet")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> deleteMyWallet(@AuthenticationPrincipal UserDetails userDetails) {
        Long authUserId = getAuthUserId(userDetails);
        Long customerId = getCustomerId(authUserId);
        WalletDTO wallet = walletService.getWalletByCustomerId(customerId);

        walletService.deleteWalletById(wallet.getId());
        return ResponseEntity.ok("Your wallet has been deleted.");
    }

    /* UTILITY */

    private Long getAuthUserId(UserDetails userDetails) {
        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) {
            throw new UnauthorizedException("User not found");
        }
        return authUser.getId();
    }

    private Long getCustomerId(Long authUserId) {
        CustomerDTO customer = customerService.getCustomerByAuthUserId(authUserId);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found for current user");
        }
        return customer.getId();
    }
}

