package com.martishyn.productapi.service;

import com.martishyn.productapi.dto.CreateCustomerRequest;
import com.martishyn.productapi.exceptions.CustomerNotFoundException;
import com.martishyn.productapi.model.Customer;
import com.martishyn.productapi.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer(request.getName(), request.getEmail());
        return customerRepository.save(customer);
    }

    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    public Customer findCustomerByName(String name) {
        return customerRepository.findCustomerByName(name).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }
}
