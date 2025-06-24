package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.CustomerDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ConflictException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.CustomerMapper;
import com.petraccia.elisabetta.CretaceousPark.model.Customer;
import com.petraccia.elisabetta.CretaceousPark.repository.CustomerRepository;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.repository.AuthUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AuthUserRepository authUserRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AuthUserRepository authUserRepository) {
        this.customerRepository = customerRepository;
        this.authUserRepository = authUserRepository;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toDTO)
                .toList();
    }

    public CustomerDTO getCustomerById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        return CustomerMapper.toDTO(customer);
    }

    public boolean existsByAuthUserId(AuthUser authUser) {
        return customerRepository.existsByAuthUser(authUser);
    }

    public CustomerDTO getCustomerByAuthUserId(Long authUserId) {
        if (authUserId == null) {
            throw new BadRequestException("AuthUser ID must not be null.");
        }

        Customer customer = customerRepository.findByAuthUserId(authUserId);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found with AuthUser ID: " + authUserId);
        }

        return CustomerMapper.toDTO(customer);
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        if (customerDTO.getAuthUserId() == null) {
            throw new BadRequestException("AuthUser ID must not be null.");
        }

        AuthUser authUser = authUserRepository.findById(customerDTO.getAuthUserId())
                .orElseThrow(() -> new ResourceNotFoundException("AuthUser not found with id: " + customerDTO.getAuthUserId()));

        if (customerRepository.existsByAuthUser(authUser)) {
            throw new ConflictException("This AuthUser is already assigned to another Admin/Customer.");
        }

        // Non passiamo Wallet all'inizio perché è null alla creazione
        Customer customerToSave = CustomerMapper.toEntity(customerDTO, authUser, null);
        Customer savedCustomer = customerRepository.save(customerToSave);
        return CustomerMapper.toDTO(savedCustomer);
    }

    @Transactional
    public void deleteCustomerById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        AuthUser user = customer.getAuthUser();
        if (user != null) {
            user.setCustomer(null);
            authUserRepository.save(user);
        }

        customerRepository.deleteById(id);
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        if (customerDTO == null) {
            throw new BadRequestException("Customer data must not be null.");
        }

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        AuthUser authUser = null;
        if (customerDTO.getAuthUserId() != null) {
            authUser = authUserRepository.findById(customerDTO.getAuthUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("AuthUser not found with id: " + customerDTO.getAuthUserId()));
        }

        existingCustomer.setFirstname(customerDTO.getFirstname());
        existingCustomer.setLastname(customerDTO.getLastname());
        existingCustomer.setBirthdate(customerDTO.getBirthdate());
        existingCustomer.setAuthUser(authUser);
        // Il wallet resta invariato, o può essere gestito separatamente

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return CustomerMapper.toDTO(updatedCustomer);
    }
}
