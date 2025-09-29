package com.martishyn.productapi.service;

import com.martishyn.productapi.dto.CreateCustomerRequest;
import com.martishyn.productapi.exceptions.CustomerNotFoundException;
import com.martishyn.productapi.model.Customer;
import com.martishyn.productapi.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public Customer createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer(request.getName(), request.getEmail());
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    @Transactional(readOnly = true)
    public Customer findCustomerByName(String name) {
        return customerRepository.findCustomerByName(name).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    @Transactional
    public void deleteCustomer(Long customerId) {
        Customer foundCustomer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customerRepository.delete(foundCustomer);
    }
}
