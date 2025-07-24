package com.martishyn.week1.src.generics;

import java.util.List;

public class GenericUtils {

    public static void printAll(List<?> list) {
        list.forEach(System.out::println);
    }

    public static double sumNumbers(List<? extends Number> list) {
        return list.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }

    public static <T> void copy(List<? super T> dest, List<T> src) {
        src.stream()
                .map(dest::add);
    }

    public static <T extends Comparable<T>> T findMax(List<T> list) {
        return null;
    }


}
