package com.martishyn.week1.src.generics;

public class Box<T> {
    T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
