package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.WalletDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Customer;
import com.petraccia.elisabetta.CretaceousPark.model.Wallet;

public class WalletMapper {

    public static WalletDTO toDTO(Wallet wallet) {
        if (wallet == null) return null;

        return WalletDTO.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .createdAt(wallet.getCreatedAt())
                .updatedAt(wallet.getUpdatedAt())
                .customerId(wallet.getCustomer() != null ? wallet.getCustomer().getId() : null)
                .build();
    }

    public static Wallet toEntity(WalletDTO dto, Customer customer) {
        if (dto == null) return null;

        return Wallet.builder()
                .id(dto.getId())
                .balance(dto.getBalance())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .customer(customer)
                .build();
    }
}

