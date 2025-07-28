package com.martishyn.week1.src.comparison;

import java.util.Comparator;

public class Product {
    String name;
    double price;
    int rating;


    @Override
    public String toString() {
        return String.format("Product{name='%s', price=%f, rating=%d}", name, price, rating);
    }

    static Comparator<? super Product> productRatingComparator() {
        return Comparator.comparing((Product product )-> product.rating).thenComparing((o1, o2) -> o1.price < o2.price ? -1 : 1);
    }
}
