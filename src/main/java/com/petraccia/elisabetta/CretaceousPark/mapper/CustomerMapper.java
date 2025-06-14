package com.petraccia.elisabetta.CretaceousPark.mapper;

import com.petraccia.elisabetta.CretaceousPark.dto.CustomerDTO;
import com.petraccia.elisabetta.CretaceousPark.model.Customer;
import com.petraccia.elisabetta.CretaceousPark.model.Wallet;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;

public class CustomerMapper {

    public static CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;

        return new CustomerDTO(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getBirthdate(),
                customer.getAuthUser() != null ? customer.getAuthUser().getId() : null,
                customer.getWallet() != null ? customer.getWallet().getId() : null
        );
    }

    public static Customer toEntity(CustomerDTO dto, AuthUser authUser, Wallet wallet) {
        if (dto == null) return null;

        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setFirstname(dto.getFirstname());
        customer.setLastname(dto.getLastname());
        customer.setBirthdate(dto.getBirthdate());
        customer.setAuthUser(authUser);
        customer.setWallet(wallet);
        return customer;
    }

}
