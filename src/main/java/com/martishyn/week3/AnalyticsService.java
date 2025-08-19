package com.martishyn.week3;

import org.junit.platform.commons.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class AnalyticsService {

    private final List<Transaction> transactions;
    private final List<Order> orders;
    private final List<User> users;
    private final List<Product> products;

    public AnalyticsService(List<Transaction> transactions,
                            List<Order> orders,
                            List<User> users,
                            List<Product> products) {
        this.transactions = transactions;
        this.orders = orders;
        this.users = users;
        this.products = products;
    }

    /**
     * Групує витрати по користувачах
     *
     * @return Map<UserName, TotalAmount>
     */
    public Map<String, Double> totalSpentByUser() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getUser, Collectors.summingDouble(Transaction::getAmount)));
    }

    /**
     * Розділяє користувачів на тих, у кого є email, і тих, у кого його немає
     *
     * @return Map<HasEmail, List < User>>
     */
    public Map<Boolean, List<User>> partitionUsersByEmail() {
        return users.stream()
                .collect(Collectors.groupingBy(c -> !StringUtils.isBlank(c.getEmail()), Collectors.toList()));
    }

    /**
     * Для кожної категорії повертає топ-N продуктів за рейтингом
     *
     * @param n кількість продуктів
     * @return Map<Category, List < Product>>
     */
    public Map<String, List<Product>> topNProductsPerCategory(int n) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.mapping(a ->
                                new Product(a.getName(), a.getCategory(), a.getPrice(), a.getRating()),
                        collectingAndThen(toList(), x ->
                                x.stream().sorted(Comparator.comparing(Product::getRating).reversed()).limit(n).toList()))));
    }

    /**
     * Знаходить найпопулярнішу роль серед усіх користувачів
     *
     * @return роль (String)
     */
    public String mostPopularRole(List<User> users) {
        final Map<String, Long> collect = users.stream()
                .flatMap(u -> u.getRoles().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return Objects.requireNonNull(collect.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .orElse(null)).getKey();
    }

    /**
     * . Згрупувати замовлення за статусом та порахувати
     */
    public Map<String, Long> countOrdersByStatus() {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
    }

    /**
     * . Клієнт з найбільшою кількістю замовлень
     */
    public String topUserByOrders() {
        Map<String, Long> collect = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomers, Collectors.counting()));
        return collect.entrySet()
                .stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Згрупувати замовлення по клієнтах
     */
    public Map<String, List<Order>> groupOrdersByCustomer() {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomers));
    }

    /**
     * 6. Відсортувати замовлення за статусом і id
     */
    public List<Order> sortOrdersByStatusAndId() {
        return orders.stream()
                .sorted(Comparator.comparing(Order::getStatus).thenComparing(Order::getId))
                .collect(Collectors.toList());
    }
}
