package com.martishyn.week1.src.comparison;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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

        System.out.println("+===================================+");

        Task task1 = new Task();
        task1.title = "Buy milk";
        task1.scheduledAt = LocalDate.of(2021, 12, 4);
        task1.priority = 1;

        Task task2 = new Task();
        task2.title = "Buy eggs";
        task2.scheduledAt = LocalDate.of(2021, 12, 2);
        task2.priority = 2;

        Task task3 = new Task();
        task3.title = "Buy bread";
        task3.scheduledAt = LocalDate.of(2021, 12, 3);
        task3.priority = 3;

        Task task4 = new Task();
        task4.title = "Buy sugar";
        task4.scheduledAt = LocalDate.of(2021, 12, 1);
        task4.priority = 4;

        Task task5 = new Task();
        task5.title = "Buy sugar";
        task5.scheduledAt = LocalDate.of(2021, 12, 7);
        task5.priority = 2;

        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        tasks.add(task4);
        tasks.sort(Task.taskDeadlineComparator());
        System.out.printf("tasks = %s", tasks);

        System.out.println("+===================================+");

        tasks.sort(Task.taskDeadlineTitleComparator());
        System.out.printf("tasks = %s", tasks);

        System.out.println("+===================================+");
        TreeSet<Task> tasksByTitle = new TreeSet<>(Comparator.comparing(Task::getTitle));
        tasksByTitle.add(task1);
        tasksByTitle.add(task2);
        tasksByTitle.add(task3);
        tasksByTitle.add(task4);
        tasksByTitle.add(task5);
        System.out.printf("tasksByTitle = %s", tasksByTitle);
    }
}
