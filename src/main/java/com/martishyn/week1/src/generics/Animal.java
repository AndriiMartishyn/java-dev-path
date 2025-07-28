package com.martishyn.week1.src.generics;

public class Animal {
    public String makeSound() {
        return "Some sound";
    }


    public static class Dog extends Animal {
        @Override
        public String makeSound() {
            return "Woof";
        }
    }

    public static class Cat extends Animal {
        @Override
        public String makeSound() {
            return "Meow";
        }
    }
}