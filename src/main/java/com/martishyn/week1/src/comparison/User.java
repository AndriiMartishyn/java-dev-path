package com.martishyn.week1.src.comparison;

import java.util.Comparator;


public class User implements Comparable<User> {
    String name;
    Integer age;
    double score;

    @Override
    public int compareTo(User o) {
        return this.name.compareTo(o.name);
    }

    static Comparator<? super User> userAgeComparator() {
        return Comparator.comparing(user -> user.age);
    }


    static Comparator<? super User> userScoreComparator() {
        return Comparator.comparing(user -> user.score);
    }

    static Comparator<? super User> userAgeAndNameComparator() {
        return Comparator.comparing((User o) -> o.age).thenComparing(user -> user.name);
    }

    @Override
    public String toString() {
        return String.format("User{name='%s', age=%d, score=%f}", name, age, score);
    }
}
