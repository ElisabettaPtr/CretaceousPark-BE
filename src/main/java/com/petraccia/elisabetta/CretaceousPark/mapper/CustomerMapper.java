package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.CustomerDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Customer;
import com.petraccia.elisabetta.CretaceousPark.model.Wallet;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;

public class CustomerMapper {

    public static CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;

        return CustomerDTO.builder()
                .id(customer.getId())
                .firstname(customer.getFirstname())
                .lastname(customer.getLastname())
                .birthdate(customer.getBirthdate())
                .authUserId(customer.getAuthUser() != null ? customer.getAuthUser().getId() : null)
                .walletId(customer.getWallet() != null ? customer.getWallet().getId() : null)
                .build();
    }

    public static Customer toEntity(CustomerDTO dto, AuthUser authUser, Wallet wallet) {
        if (dto == null) return null;

        return Customer.builder()
                .id(dto.getId())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .birthdate(dto.getBirthdate())
                .authUser(authUser)
                .wallet(wallet)
                .build();
    }
}
