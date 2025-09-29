package com.martishyn.productapi.repository;

import com.martishyn.productapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.name = :name")
    Optional<Customer> findCustomerByName(String name);

    Optional<Customer> findCustomerByEmail(String email);
}
