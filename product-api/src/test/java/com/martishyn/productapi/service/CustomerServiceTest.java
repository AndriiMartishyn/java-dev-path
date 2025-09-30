package com.martishyn.productapi.service;

import com.martishyn.productapi.dto.CreateCustomerRequest;
import com.martishyn.productapi.exceptions.CustomerNotFoundException;
import com.martishyn.productapi.model.Customer;
import com.martishyn.productapi.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateCustomer() {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setName("test");
        createCustomerRequest.setEmail("<EMAIL>");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("test");
        customer.setEmail("<EMAIL>");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer createdCustomer = customerService.createCustomer(createCustomerRequest);

        Assertions.assertEquals(1L, createdCustomer.getId());
        Assertions.assertEquals("test", createdCustomer.getName());
        Assertions.assertEquals("<EMAIL>", createdCustomer.getEmail());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void shouldFindCustomerById() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("test");
        customer.setEmail("<EMAIL>");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer customerById = customerService.findCustomerById(1L);

        Assertions.assertEquals(1L, customerById.getId());
        Assertions.assertEquals("test", customerById.getName());
        Assertions.assertEquals("<EMAIL>", customerById.getEmail());
        verify(customerRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenFindCustomerByIdWithNullIdParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> customerService.findCustomerById(null));
    }

    @Test
    void shouldThrowExceptionWhenFindCustomerByIdWithNegativeIdParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> customerService.findCustomerById(-1L));
    }

    @Test
    void shouldFindCustomerByName() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("test");
        customer.setEmail("<EMAIL>");

        when(customerRepository.findCustomerByName("test")).thenReturn(Optional.of(customer));

        Customer customerByName = customerService.findCustomerByName("test");

        Assertions.assertEquals(1L, customerByName.getId());
        Assertions.assertEquals("test", customerByName.getName());
        Assertions.assertEquals("<EMAIL>", customerByName.getEmail());
        verify(customerRepository).findCustomerByName("test");
    }

    @Test
    void shouldThrowExceptionWhenFindCustomerByNameWithWrongNameParameter() {
        when(customerRepository.findCustomerByName("test")).thenReturn(Optional.empty());

        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService.findCustomerByName("test"));
    }

    @Test
    void shouldThrowExceptionWhenFindCustomerByNameWithBlankNameParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> customerService.findCustomerByName(""));
    }

    @Test
    void shouldThrowExceptionWhenFindCustomerByNameWithNullNameParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> customerService.findCustomerByName(null));
    }

    @Test
    void shouldThrowExceptionWhenDeleteCustomerWIthNullId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> customerService.deleteCustomer(null));
    }

    @Test
    void shouldThrowExceptionWhenDeleteCustomerWithWrongId() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(1L));
    }

    @Test
    void shouldDeleteCustomerById() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("test");
        customer.setEmail("<EMAIL>");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        ArgumentCaptor<Customer> capture = ArgumentCaptor.forClass(Customer.class);

        customerService.deleteCustomer(1L);

        verify(customerRepository).findById(1L);
        verify(customerRepository).delete(capture.capture());
        Assertions.assertEquals(1L, capture.getValue().getId());
        Assertions.assertEquals("test", capture.getValue().getName());
        Assertions.assertEquals("<EMAIL>", capture.getValue().getEmail());
    }
}
