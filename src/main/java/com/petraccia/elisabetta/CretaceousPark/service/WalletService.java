package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.WalletDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ConflictException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.WalletMapper;
import com.petraccia.elisabetta.CretaceousPark.model.Customer;
import com.petraccia.elisabetta.CretaceousPark.model.Wallet;
import com.petraccia.elisabetta.CretaceousPark.repository.CustomerRepository;
import com.petraccia.elisabetta.CretaceousPark.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository, CustomerRepository customerRepository) {
        this.walletRepository = walletRepository;
        this.customerRepository = customerRepository;
    }

    public List<WalletDTO> getAllWallets() {
        return walletRepository.findAll()
                .stream()
                .map(WalletMapper::toDTO)
                .toList();
    }

    public WalletDTO getWalletById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with id: " + id));

        return WalletMapper.toDTO(wallet);
    }

    public WalletDTO getWalletByCustomerId(Long customerId) {
        if (customerId == null) {
            throw new BadRequestException("Customer ID must not be null.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Wallet wallet = walletRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for Customer id: " + customerId));

        return WalletMapper.toDTO(wallet);
    }

    public WalletDTO saveWallet(WalletDTO walletDTO) {
        if (walletDTO.getCustomerId() == null) {
            throw new BadRequestException("Customer ID must not be null.");
        }

        Customer customer = customerRepository.findById(walletDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + walletDTO.getCustomerId()));

        // Se vuoi evitare più wallet per lo stesso customer, controlla qui
        if (walletRepository.existsByCustomer(customer)) {
            throw new ConflictException("This Customer already has a Wallet.");
        }

        Wallet walletToSave = WalletMapper.toEntity(walletDTO, customer);
        Wallet savedWallet = walletRepository.save(walletToSave);
        return WalletMapper.toDTO(savedWallet);
    }

    @Transactional
    public void deleteWalletById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with id: " + id));

        // Stacca il wallet dal customer per evitare riferimenti pendenti
        Customer customer = wallet.getCustomer();
        if (customer != null) {
            customer.setWallet(null);
            customerRepository.save(customer);
        }

        walletRepository.deleteById(id);
    }

    public WalletDTO updateWallet(Long id, WalletDTO walletDTO) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }
        if (walletDTO == null) {
            throw new BadRequestException("Wallet data must not be null.");
        }

        Wallet existingWallet = walletRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with id: " + id));

        Customer customer = null;
        if (walletDTO.getCustomerId() != null) {
            customer = customerRepository.findById(walletDTO.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + walletDTO.getCustomerId()));
        }

        existingWallet.setBalance(walletDTO.getBalance());
        existingWallet.setCreatedAt(walletDTO.getCreatedAt());
        existingWallet.setUpdatedAt(walletDTO.getUpdatedAt());
        existingWallet.setCustomer(customer);

        Wallet updatedWallet = walletRepository.save(existingWallet);
        return WalletMapper.toDTO(updatedWallet);
    }
}

