package com.martishyn.week1.src.generics;

import com.martishyn.week1.src.model.Book;
import com.martishyn.week1.src.model.Genre;

public class Check {
    public static void main(String[] args) {
        Box<String> stringBox = new Box<>();
        stringBox.set("test");

       // stringBox.set(1);

        Pair<Long, String> idToPlayer = new Pair<>(1L, "Andrii");
        //Pair<String, Long> idToPlayer1 = new Pair<>("test", "1L");


        GenericRepository<Book> repository = new GenericBookRepository();
        repository.save(new Book("test", "test1", Genre.BIOGRAPHY));
        System.out.println(repository.findAll());
    }
}
