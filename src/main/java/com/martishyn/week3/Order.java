package com.martishyn.week3;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Order {
    String id;
    String customers;
    String status;

    public Order(String id, String customers, String status) {
        this.id = id;
        this.customers = customers;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getCustomers() {
        return customers;
    }

    public String getStatus() {
        return status;
    }

    public static Map<String, Long> groupOrdersByStatus(List<Order> orders){
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
    }

    public static List<String> collectAndOrderRoles(Map<String, List<String>> userRoles){
        return userRoles.entrySet()
                .stream()
                .flatMap(a -> a.getValue().stream())
                .sorted(Comparator.naturalOrder())
                .distinct()
                .collect(Collectors.toList());
    }
}
