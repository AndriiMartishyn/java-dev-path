package com.martishyn.week1.src.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenericRepository<T> {
    private final List<T> storage = new ArrayList<>();

    public void save(T item){
        Objects.requireNonNull(item);
        storage.add(item);
    }
    public List<T> findAll(){
        if (storage.isEmpty()){
            throw new UnsupportedOperationException();
        }
        return storage;
    }
}

