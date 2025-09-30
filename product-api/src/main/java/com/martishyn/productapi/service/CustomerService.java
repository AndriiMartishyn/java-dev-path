package com.martishyn.productapi.service;

import com.martishyn.productapi.dto.CreateCustomerRequest;
import com.martishyn.productapi.exceptions.CustomerNotFoundException;
import com.martishyn.productapi.model.Customer;
import com.martishyn.productapi.repository.CustomerRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.Positive;
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
        if (id == null || id < 1L) {
            throw new IllegalArgumentException("Customer id must be greater than zero");
        }
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    @Transactional(readOnly = true)
    public Customer findCustomerByName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Customer name must not be blank");
        }
        return customerRepository.findCustomerByName(name).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    @Transactional
    public void deleteCustomer(Long id) {
        if (id == null || id < 1L) {
            throw new IllegalArgumentException("Customer id must be greater than zero");
        }
        Customer foundCustomer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customerRepository.delete(foundCustomer);
    }
}
