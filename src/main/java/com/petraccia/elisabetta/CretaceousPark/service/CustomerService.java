package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ConflictException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.model.Admin;
import com.petraccia.elisabetta.CretaceousPark.model.Customer;
import com.petraccia.elisabetta.CretaceousPark.repository.CustomerRepository;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.repository.AuthUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final AuthUserRepository authUserRepository;

    @Autowired
    public CustomerService (CustomerRepository customerRepository, AuthUserRepository authUserRepository){
        this.customerRepository = customerRepository;
        this.authUserRepository = authUserRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

    }

    public Customer getCustomerByAuthUserId(Long authUserId) {

        if (authUserId == null) {
            throw new BadRequestException("AuthUser ID must not be null.");
        }

        Customer customer = customerRepository.findByAuthUserId(authUserId);

        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found with AuthUser ID: " + authUserId);
        }

        return customer;

    }

    public Customer saveCustomer(Customer customer) {

        if (customer.getAuthUser() == null) {
            throw new BadRequestException("AuthUser must not be null.");
        }

        Long authUserId = customer.getAuthUser().getId();
        if (authUserId == null || !authUserRepository.existsById(authUserId)) {
            throw new ResourceNotFoundException("AuthUser not found with id: " + authUserId);
        }

        if (customerRepository.existsByAuthUser(customer.getAuthUser())) {
            throw new ConflictException("This AuthUser is already assigned to another Admin/Customer.");
        }

        return customerRepository.save(customer);

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
            user.setAdmin(null);
            authUserRepository.save(user);
        }

        customerRepository.deleteById(id);

    }

    public Customer updateCustomer(Long id, Customer customer) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        if (customer == null) {
            throw new BadRequestException("Customer data must not be null.");
        }

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        existingCustomer.setFirstname(customer.getFirstname());
        existingCustomer.setLastname(customer.getLastname());
        existingCustomer.setBirthdate(customer.getBirthdate());
        existingCustomer.setWallet(customer.getWallet());
        existingCustomer.setAuthUser(customer.getAuthUser());

        return customerRepository.save(existingCustomer);
    }

    // TODO : MODIFY -> adapt to DTO
}
