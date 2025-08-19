package com.martishyn.week3;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Product {

    String id;
    String name;
    String category;
    double price;
    double rating;

    public Product(String name, String category, double price, double rating) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.rating = rating;
    }

    public Product(String id, String name, String category, double price, double rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }

    public static List<Product> filterByPriceAndRating(List<Product> products) {
        Predicate<? super Product> priceAndRatingPredicate = (o) -> o.getPrice() < 100 && o.getRating() >= 4;
        return products.stream()
                .filter(priceAndRatingPredicate)
                .sorted(productComparator())
                .collect(Collectors.toList());
    }

    public static Comparator<? super Product> productComparator() {
         Comparator<Product> comparing = Comparator.comparing((Product::getCategory));
         Comparator<Product> reversed = Comparator.comparing(Product::getRating).reversed();
        return comparing.thenComparing(reversed);
    }
}
