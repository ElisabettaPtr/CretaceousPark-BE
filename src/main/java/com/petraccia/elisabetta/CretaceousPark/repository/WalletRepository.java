package com.petraccia.elisabetta.CretaceousPark.repository;

import com.petraccia.elisabetta.CretaceousPark.model.Customer;
import com.petraccia.elisabetta.CretaceousPark.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByCustomer(Customer customer);

    boolean existsByCustomer(Customer customer);

}
