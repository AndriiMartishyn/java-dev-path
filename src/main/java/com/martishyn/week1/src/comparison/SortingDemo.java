package com.martishyn.week1.src.comparison;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class SortingDemo {

    public static void main(String[] args) {
        User user1 = new User();
        user1.name = "Martin";
        user1.age = 25;
        user1.score = 100;

        User user2 = new User();
        user2.name = "John";
        user2.age = 30;
        user2.score = 90;

        User user3 = new User();
        user3.name = "Andrii";
        user3.age = 20;
        user3.score = 80;

        TreeSet<User> users = new TreeSet<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        System.out.printf("users = %s", users); //Andrii John Martin

        List<User> usersList = new ArrayList<>();
        usersList.add(user1);
        usersList.add(user2);
        usersList.add(user3);
        usersList.sort(User.userAgeComparator());
        System.out.printf("usersListAge = %s", usersList); //Andrii Martin John

        usersList.sort(User.userScoreComparator());
        System.out.printf("usersListScores = %s", usersList); //Andrii John Martihn

        usersList.sort(User.userAgeAndNameComparator());
        System.out.printf("usersListAgeAndName = %s", usersList); // Andrii Martin John

        Product product1 = new Product();
        product1.name = "Apple";
        product1.price = 100;
        product1.rating = 5;

        Product product2 = new Product();
        product2.name = "Banana";
        product2.price = 200;
        product2.rating = 3;

        Product product3 = new Product();
        product3.name = "Orange";
        product3.price = 500;
        product3.rating = 4;

        Product product4 = new Product();
        product4.name = "Pear";
        product4.price = 400;
        product4.rating = 4;

        System.out.println("+===================================+");
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);
        products.sort(Product.productRatingComparator());
        System.out.printf("products = %s", products);

    }
}
