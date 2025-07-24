package com.martishyn.week1.test;


import com.martishyn.week1.src.Library;
import com.martishyn.week1.src.LibraryPractice;
import com.martishyn.week1.src.model.Book;
import com.martishyn.week1.src.model.Genre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryTest {

    private final Library library = new Library();

    @Test
    public void shouldAddNewBookByGenre() {

        library.addBook(new Book("test_title", "test_author", Genre.BIOGRAPHY));
        Assertions.assertEquals(1, library.countBooksInGenre(Genre.BIOGRAPHY));

        library.addBook(new Book("test_title1", "test_author1", Genre.SCIFI));
        Assertions.assertEquals(1, library.countBooksInGenre(Genre.SCIFI));
    }

    @Test
    public void shouldCountAllBooksByGenre() {
        library.addBook(new Book("test_title", "test_author", Genre.BIOGRAPHY));
        int actual = library.countBooksInGenre(Genre.BIOGRAPHY);
        Assertions.assertEquals(1, actual);
    }

    @Test
    public void shouldReturnALlBooksByGenre() {
        library.addBook(new Book("test_title", "test_author", Genre.BIOGRAPHY));
        library.addBook(new Book("test_title2", "test_author2", Genre.BIOGRAPHY));
        library.addBook(new Book("test_title3", "test_author3", Genre.BIOGRAPHY));
        int actual = library.getBooksByGenre(Genre.BIOGRAPHY).size();
        Assertions.assertEquals(3, actual);
    }

    @Test
    public void shouldSortBooksByTitleLength() {
        library.addBook(new Book("A", "test_author", Genre.BIOGRAPHY));
        library.addBook(new Book("Dune", "test_author2", Genre.BIOGRAPHY));
        library.addBook(new Book("The Lord of The Rings", "test_author3", Genre.BIOGRAPHY));
        library.addBook(new Book("Hobbit", "test_author4", Genre.BIOGRAPHY));
        library.addBook(new Book("1984", "test_author5", Genre.BIOGRAPHY));

        final List<Book> books = LibraryPractice.sortBooksByTitleLength(library);
        books.forEach(s -> System.out.println(s.getTitle()));
    }
}
