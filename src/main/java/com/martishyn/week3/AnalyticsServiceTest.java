package com.martishyn.week3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class AnalyticsServiceTest {

    List<Transaction> transactions = List.of(
            new Transaction("t1", "u1", 120.50),
            new Transaction("t2", "u2", 250.00),
            new Transaction("t3", "u1", 300.00),
            new Transaction("t4", "u3", 80.00)
    );

    List<Order> orders = List.of(
            new Order("o1", "u1", "NEW"),
            new Order("o2", "u1", "COMPLETED"),
            new Order("o3", "u2", "NEW"),
            new Order("o4", "u2", "IN_PROGRESS"),
            new Order("o5", "u3", "COMPLETED"),
            new Order("o6", "u1", "NEW")
    );

    List<User> users = List.of(
            new User("u1", "Alice", List.of("Admin", "User"), "alice@email.com"),
            new User("u2", "Bob", List.of("User"), null), // ❌ немає email
            new User("u3", "Charlie", List.of("User", "Editor"), "charlie@email.com"),
            new User("u4", "Diana", List.of("Guest"), ""), // ❌ пустий email
            new User("u5", "Eve", List.of("User"), "eve@email.com")
    );

    List<Product> products = List.of(
            new Product("p1", "Laptop", "Electronics", 1500.00, 4.8),
            new Product("p2", "Phone", "Electronics", 800.00, 4.5),
            new Product("p3", "Headphones", "Electronics", 120.00, 4.2),
            new Product("p4", "Book A", "Books", 25.00, 4.9),
            new Product("p5", "Book B", "Books", 30.00, 4.7),
            new Product("p6", "Chair", "Furniture", 200.00, 4.3)
    );

    AnalyticsService analyticsService = new AnalyticsService(
            transactions, orders, users, products
    );

    @Test
    void shouldFindTotalSpendByUsers() {
        Map<String, Double> stringDoubleMap = analyticsService.totalSpentByUser();

        Assertions.assertEquals(420.50, stringDoubleMap.get("u1"));
        Assertions.assertEquals(250.00, stringDoubleMap.get("u2"));
        Assertions.assertEquals(80.00, stringDoubleMap.get("u3"));
    }

    @Test
    void shouldPartitionUsersByEmail(){
        Map<Boolean, List<User>> usersByEmail = analyticsService.partitionUsersByEmail();

        Assertions.assertEquals(3, usersByEmail.get(Boolean.TRUE).size());
        Assertions.assertEquals(2, usersByEmail.get(Boolean.FALSE).size());
    }

    @Test
    void shouldGroupProductsByRating(){
        Map<String, List<Product>> productByRatingLimited = analyticsService.topNProductsPerCategory(2);

        List<Product> electronics = productByRatingLimited.get("Electronics");
        Assertions.assertEquals(4.8, electronics.getFirst().getRating());
        Assertions.assertEquals(4.5, electronics.get(1).getRating());

        List<Product> books = productByRatingLimited.get("Books");
        Assertions.assertEquals("Book A", books.getFirst().getName());
        Assertions.assertEquals("Book B", books.get(1).getName());
    }

    @Test
    void shouldFindMostPopularRole(){
        String mostPopularRole = analyticsService.mostPopularRole(users);
        Assertions.assertEquals("User", mostPopularRole);
    }


    @Test
    void shouldCountOrdersByStatus(){
        Map<String, Long> ordersByStatus = analyticsService.countOrdersByStatus();

        Assertions.assertEquals(3, ordersByStatus.get("NEW"));
        Assertions.assertEquals(1, ordersByStatus.get("IN_PROGRESS"));
        Assertions.assertEquals(2, ordersByStatus.get("COMPLETED"));
    }

    @Test
    void shouldTopUserByNumOfOrders(){
        String user = analyticsService.topUserByOrders();

        Assertions.assertEquals("u1", user);
    }

    @Test
    void shouldGroupOrdersByCustomer(){
        Map<String, List<Order>> ordersByCustomer = analyticsService.groupOrdersByCustomer();

        List<Order> u1Orders = ordersByCustomer.get("u1");
        Assertions.assertEquals(3, u1Orders.size());

        List<Order> u2Orders = ordersByCustomer.get("u2");
        Assertions.assertEquals(2, u2Orders.size());

        List<Order> u3Orders = ordersByCustomer.get("u3");
        Assertions.assertEquals(1, u3Orders.size());
    }
}
