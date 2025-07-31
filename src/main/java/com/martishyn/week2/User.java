package com.martishyn.week2;

import jdk.swing.interop.SwingInterOpUtils;

import javax.management.relation.Role;
import javax.swing.KeyStroke;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.summarizingDouble;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class User {
    private String name;
    private int age;
    private String email;
    private List<String> roles;

    // constructor, getters, toString


    public User(String name, int age, String email, List<String> roles) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.roles = roles;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }

    public static void main(String[] args) {
        List<User> users = List.of(
                new User("Alice", 30, "alice@email.com", List.of("Admin", "User")),
                new User("Bob", 19, null, List.of("User")),
                new User("Charlie", 27, "charlie@email.com", List.of("User", "Editor")),
                new User("Diana", 17, "diana@email.com", List.of("Guest")),
                new User("Eve", 22, null, List.of("User")),
                new User("Frank", 25, "frank@email.com", List.of("User", "Admin"))
        );

        final List<User> usersAge18Plus = getUsersAge18Plus(users);
        usersAge18Plus.forEach(System.out::println);
        System.out.println("\n ---------------------");
        final List<User> usersWithEmails = findUsersWithEmails(users);
        usersWithEmails.forEach(System.out::println);
        System.out.println("\n ---------------------");
        User oldestUser = findOldestUsers(users);
        System.out.println(oldestUser);
        System.out.println("\n ---------------------");
        List<String> usersWithNamesUpperCase = getUsersWithNamesUpperCase(users);
        usersWithNamesUpperCase.forEach(System.out::println);
        System.out.println("\n ---------------------");
        groupUsersByRole(users);
        System.out.println("\n ---------------------");
        groupUsersCountedByRole(users);
        System.out.println("\n ---------------------");
        final Optional<User> userWithoutEmail = findUserWithoutEmail(users);
        userWithoutEmail.ifPresent(usr -> System.out.println(usr.getName()));
    }

    private static List<User> getUsersAge18Plus(List<User> users) {
        return users.stream()
                .filter(u -> u.getAge() > 18)
                .toList();
    }

    private static List<User> findUsersWithEmails(List<User> users) {
        return users.stream()
                .filter(user -> user.getEmail() != null && !user.getEmail().isEmpty())
                .toList();
    }

    private static User findOldestUsers(List<User> users) {
        return users.stream().max(Comparator.comparingInt(User::getAge))
                .orElse(null);
    }

    private static List<String> getUsersWithNamesUpperCase(List<User> users) {
        return users.stream()
                .map(user -> user.getName().toUpperCase())
                .toList();
    }

    private static void groupUsersByRole(List<User> users) {
        Map<String, Set<List<User>>> collect = users.stream()
                .flatMap(user -> user.getRoles().stream())
                .map(Role -> Map.entry(Role, users))
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, toSet())));

        for (Map.Entry<String, Set<List<User>>> e : collect.entrySet()) {
            System.out.println("Role: " + e.getKey() + ", Users: " + e.getValue()
                    .stream()
                    .flatMap(g -> g.stream())
                            .map(User::getName)
                    .collect(Collectors.joining(",")));
        }
    }

    private static void groupUsersCountedByRole(List<User> users) {
        Map<String, List<List<User>>> collect = users.stream()
                .flatMap(user -> user.getRoles().stream())
                .map(Role -> Map.entry(Role, users))
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, toList())));

        for (Map.Entry<String, List<List<User>>> e : collect.entrySet()) {
            System.out.println("Role: " + e.getKey() + ", Users: " + e.getValue().size());
        }
    }

//    private static void checkIfAllUsersHaveAtLeast1Role(List<User> users) {
//        Map<String, List<List<User>>> collect = users.stream()
//                .flatMap(user -> user.getRoles().stream())
//                .map(Role -> Map.entry(Role, users))
//                .collect(Collectors.groupingBy(Map.Entry::getKey,
//                        Collectors.mapping(Map.Entry::getValue, toList())));
//
//        for (Map.Entry<String, List<List<User>>> e : collect.entrySet()) {
//            System.out.println("Role: " + e.getKey() + ", Users: " + e.getValue().size());
//        }
//    }

    private static Optional<User> findUserWithoutEmail(List<User> users){
        return users.stream()
                .filter(user -> user.getEmail() == null)
                .findAny();
    }
}
