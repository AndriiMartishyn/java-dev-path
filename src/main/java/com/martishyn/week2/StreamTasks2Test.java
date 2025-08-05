package com.martishyn.week2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class StreamTasks2Test {

    private final List<User> users = List.of(
            new User("Alice", 30, "alice@email.com", List.of("Admin", "User")),
            new User("Bob", 19, null, List.of("User")),
            new User("Charlie", 27, "charlie@email.com", List.of("User", "Editor")),
            new User("Diana", 17, "diana@email.com", List.of("Guest")),
            new User("Eve", 22, null, List.of("User")),
            new User("Frank", 25, "frank@email.com", List.of("User", "Admin"))
    );

    @Test
    public void shouldSortByEmailAndName() {
        final Map<String, String> test = StreamTasks2.mapEmailToName(users);
        test.entrySet().forEach(System.out::println);
    }

    @Test
    public void shouldFindAdmins() {
        List<User> test = StreamTasks2.findAdmins(users);
        test.forEach(System.out::println);
    }

    @Test
    public void shouldCheckUserRoles() {
        boolean b = StreamTasks2.allUsersHaveRoles(users);
        Assertions.assertTrue(b);
    }

    @Test
    public void shouldCheckIfAnyUserWithoutEmail() {
        boolean b = StreamTasks2.anyUserWithoutEmail(users);
        Assertions.assertTrue(b);
    }

    @Test
    public void checkAllUniqueRoles() {
        Set<String> allUniqueRoles = StreamTasks2.getAllUniqueRoles(users);
        Assertions.assertEquals(4, allUniqueRoles.size());
    }

    @Test
    public void countUsersByRole() {
        final Map<String, Long> test = StreamTasks2.countUsersByRole(users);
        test.entrySet().forEach(System.out::println);
    }

    @Test
    public void groupUserNamesByAge() {
        final Map<Integer, List<String>> test = StreamTasks2.groupUserNamesByAge(users);
        test.entrySet().forEach(System.out::println);
    }

    @Test
    public void findYoungestWithRoleUser() {
        Optional<User> user =  StreamTasks2.findYoungestWithRoleUser(users);
        Assertions.assertEquals(users.get(1), user.get());
    }

    @Test
    public void groupUsersByFirstLetter() {
        Map<Character, List<User>> test =  StreamTasks2.groupUsersByFirstLetter(users);
        test.entrySet().forEach(System.out::println);
    }

    @Test
    public void getAverageAgeWithEmail() {
        double result =  StreamTasks2.getAverageAgeWithEmail(users);
        Assertions.assertEquals(24.75, result);
    }


}
