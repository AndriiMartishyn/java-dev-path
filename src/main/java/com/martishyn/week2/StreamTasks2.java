package com.martishyn.week2;

import org.xml.sax.ext.Attributes2;

import javax.management.relation.Role;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class StreamTasks2 {

    // Завдання 1: Map<String, String>, де ключ – email, значення – ім’я
    public static Map<String, String> mapEmailToName(List<User> users) {
        Map<String, String> collect = users.stream()
                .filter(user -> user.getEmail() != null)
                .collect(Collectors.toMap(User::getEmail, User::getName));
        return collect;
    }

    // Завдання 2: Знайди всіх користувачів із роллю "Admin"
    public static List<User> findAdmins(List<User> users) {
        return users.stream()
                .filter(user -> user.getRoles().contains("Admin"))
                .collect(toList());
    }

    // Завдання 3: Перевір, чи всі користувачі мають хоча б одну роль
    public static boolean allUsersHaveRoles(List<User> users) {
        Predicate<? super User> roleExistsPredicate = (u) -> !u.getRoles().isEmpty();
        return users.stream()
                .allMatch(roleExistsPredicate);
    }

    // Завдання 4: Перевір, чи є користувачі без email
    public static boolean anyUserWithoutEmail(List<User> users) {
        return users.stream()
                .anyMatch(user -> user.getEmail() != null);
    }

    // Завдання 5: Отримай множину всіх унікальних ролей
    public static Set<String> getAllUniqueRoles(List<User> users) {
        return users.stream()
                .flatMap(user -> user.getRoles().stream())
                .collect(Collectors.toSet());
    }

    // Завдання 6: Поверни Map з роллю як ключем і кількістю користувачів з цією роллю
    public static Map<String, Long> countUsersByRole(List<User> users) {
        return users.stream()
                .flatMap(user -> user.getRoles().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    // Завдання 7: Поверни Map, де ключ — вік, значення — список імен користувачів з таким віком
    public static Map<Integer, List<String>> groupUserNamesByAge(List<User> users) {
        return users.stream()
                .collect(Collectors.groupingBy(User::getAge, Collectors.mapping(User::getName, toList())));
    }

    // Завдання 8: Знайди наймолодшого користувача з роллю "User"
    public static Optional<User> findYoungestWithRoleUser(List<User> users) {
        return users.stream()
                .filter(user -> user.getRoles().contains("User"))
                .min(Comparator.comparing(User::getAge));
    }

    // Завдання 9: Поверни Map, де ключ — перша літера імені, значення — список користувачів з такою літерою
    public static Map<Character, List<User>> groupUsersByFirstLetter(List<User> users) {
        return users.stream()
                .collect(Collectors.groupingBy(user -> user.getName().charAt(0),
                        Collectors.mapping(Function.identity(), toList())));
    }

    // Завдання 10: Порахуй середній вік користувачів, які мають email
    public static double getAverageAgeWithEmail(List<User> users) {
        final long totalUsersWithEmail = users.stream()
                .filter(user -> user.getEmail() != null)
                .count();
        double sum = users.stream()
                .filter(user -> user.getEmail() != null)
                .map(User::getAge)
                .mapToDouble(Integer::doubleValue)
                .sum();
        return sum / totalUsersWithEmail;
    }
}
