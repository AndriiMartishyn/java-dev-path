package com.martishyn.week1.src.generics;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
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

//    public static <T> T covariance(List<? extends Animal> list) {
//        list.add(new Animal.Cat());
//    }


//    public static <T> T contravariance(List<? super Animal.Dog> list) {
//        list.add(new Animal.Dog());
//        list.add(new Animal.Cat());
//        list.add(new Animal());
//    }

//    public static <T  extends Comparable<T>> T findMax(List<T> list) {
//        return null;
//    }

    public static <T extends Animal> void yell(List<T> animals){
     //   animals.forEach(Animal::makeSound);

    }
}
