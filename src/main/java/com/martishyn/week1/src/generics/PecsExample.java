package com.martishyn.week1.src.generics;

import java.util.*;
import java.util.function.Predicate;

public class PecsExample {

    // Producer: читаємо зі списку тварин
    public static void printSounds(List<? extends Animal> animals) {
        for (Animal a : animals) {
            System.out.println(a.makeSound());
        }
    }

    // Consumer: додаємо собак у список
    public static void addDogs(List<? super Animal.Dog> list) {
        list.add(new Animal.Dog());
        list.add(new Animal.Dog());
    }

    // Комбінований метод: копіюємо елементи
    public static <T> void copyList(List<? super T> dest, List<? extends T> src) {
        for (T t : src) {
            dest.add(t);
        }
    }

    // Фільтрація з Predicate<? super T>
    public static <T> List<T> filter(List<T> list, Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T t : list) {
            if (predicate.test(t)) {
                result.add(t);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Animal.Dog> dogs = new ArrayList<>();
        addDogs(dogs);

        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal.Cat());

        // Producer: читаємо звуки
        System.out.println("Sounds from dogs:");
        printSounds(dogs);  // працює, бо List<Dog> extends List<? extends Animal>

        System.out.println("Sounds from animals:");
        printSounds(animals);

        // Consumer: додаємо собак у список тварин
        addDogs(animals);
        System.out.println("Animals after adding dogs:");
        printSounds(animals);

        // Копіюємо з dogs у animals
        copyList(animals, dogs);
        System.out.println("Animals after copy:");
        printSounds(animals);

        // Фільтрація — отримуємо лише собак
        List<Animal> mixed = new ArrayList<>();
        mixed.add(new Animal.Dog());
        mixed.add(new Animal.Cat());
        mixed.add(new Animal.Dog());

        List<Animal> dogsOnly = filter(mixed, a -> a instanceof Animal.Dog);
        System.out.println("Filtered dogs:");
        printSounds(dogsOnly);

        System.out.println("-------------------------------");
        List<Number> numbers = new ArrayList<>();
        append(numbers, 1);
        numbers.add(2.2);

    }

    public static void append(Collection<? super Integer> integers, int n) {
        for (int i = 1; i <= n; i++) {
            integers.add(i);
        }
    }
}

