package com.martishyn.week3;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

public class TasksTest {

    @Test
    void testFilterAndSortProducts() {
        List<Product> products = List.of(
                new Product("Phone", "Electronics", 99.99, 4.5),
                new Product("Laptop", "Electronics", 1200.0, 4.8),
                new Product("Mug", "Home", 15.0, 4.2),
                new Product("Lamp", "Home", 45.0, 3.8),
                new Product("Book", "Books", 12.5, 4.9)
        );

        List<Product> result = Product.filterByPriceAndRating(products);

        assertEquals(3, result.size());
        assertEquals("Books", result.get(0).getCategory());
    }

    @Test
    void testMapCustomersToEmails() {
        List<Customer> customers = List.of(
                new Customer("Alice", "alice@mail.com"),
                new Customer("Bob", null),
                new Customer("Charlie", "charlie@mail.com"),
                new Customer("Diana", "")
        );

        Map<String, String> result = Customer.getCustomersByName(customers);

        assertEquals(2, result.size());
        assertTrue(result.containsKey("Alice"));
        assertFalse(result.containsKey("Bob"));
    }

    @Test
    void testTransactionStats() {
        List<Transaction> transactions = List.of(
                new Transaction("t1", "Alice", 50.0),
                new Transaction("t2", "Bob", 150.0),
                new Transaction("t3", "Alice", 70.0),
                new Transaction("t4", "Bob", 200.0)
        );

        double total = Transaction.calculateSumOfTransactions(transactions);
        assertEquals(470.0, total);

        String topUser = Transaction.findUserWithBiggestTransaction(transactions);
        assertEquals("Bob", topUser);
    }

    @Test
    void testGroupOrdersByStatus() {
        List<Order> orders = List.of(
                new Order("o1", "Alice", "NEW"),
                new Order("o2", "Bob", "PROCESSING"),
                new Order("o3", "Alice", "COMPLETED"),
                new Order("o4", "Bob", "COMPLETED"),
                new Order("o5", "Charlie", "NEW"),
                new Order("o6", "Diana", "PROCESSING"),
                new Order("o7", "Bob", "NEW")
        );

        Map<String, Long> grouped = Order.groupOrdersByStatus(orders);

        assertEquals(3, grouped.get("NEW"));
        assertEquals(2, grouped.get("PROCESSING"));
        assertEquals(2, grouped.get("COMPLETED"));
    }

    @Test
    void testUniqueRoles() {
        Map<String, List<String>> userRoles = new HashMap<>();
        userRoles.put("Alice", List.of("Admin", "User"));
        userRoles.put("Bob", List.of("User", "Editor"));
        userRoles.put("Charlie", List.of("Admin", "Manager"));

        List<String> uniqueRoles = Order.collectAndOrderRoles(userRoles);

        assertEquals(List.of("Admin", "Editor", "Manager", "User"), uniqueRoles);
    }
}
