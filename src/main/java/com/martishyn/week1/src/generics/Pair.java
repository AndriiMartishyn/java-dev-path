package com.martishyn.week1.src.generics;

public class Pair<T, U> {

    private final T firstValue;
    private final U secondValue;

    public Pair(T firstValue, U secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public T getFirst() {
        return firstValue;
    }

    public U getSecond() {
        return secondValue;
    }
}
