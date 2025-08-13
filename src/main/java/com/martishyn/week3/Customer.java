package com.martishyn.week3;

import org.junit.platform.commons.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Customer {

    String name;
    String email;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static Map<String, String> getCustomersByName(List<Customer> customers){
        return customers.stream()
                .filter(customer -> !StringUtils.isBlank(customer.email))
                .collect(Collectors.toMap(Customer::getName, Customer::getEmail));
    }
}
