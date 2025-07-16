package com.martishyn.week1.src;

import com.martishyn.week1.src.model.Book;
import com.martishyn.week1.src.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Library {
    private Map<Genre, List<Book>> booksByGenre = new HashMap<>();

    public Library() {
        Set<Map.Entry<Genre, List<Book>>> entries = booksByGenre.entrySet();
        for (var entry : entries) {
            if (entry.getKey() == null) {
                booksByGenre.put(entry.getKey(), new ArrayList<>());
            }
        }
    }

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException();
        }
        List<Book> books = booksByGenre.get(book.getGenre()); //another object, not a reference to the list in map
        if (books == null){
            books = new ArrayList<>();
        }
        books.add(book);
        booksByGenre.put(book.getGenre(), books);
    }

    public List<Book> getBooksByGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException();
        }
        return booksByGenre.get(genre);
    }

    public int countBooksInGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException();
        }
        return booksByGenre.get(genre).size();
    }

    public void removeDuplicateBooks() {
        List<Book> sortedBooks = booksByGenre.values()
                .stream()
                .flatMap(Collection::stream)
                .distinct()
                .toList();
        Genre genre = sortedBooks.getFirst().getGenre();
        booksByGenre.put(genre, sortedBooks);
    }

    public void sortBooksByTitle(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException();
        }
        List<Book> sortedBooks = booksByGenre.values()
                .stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Book::getTitle))
                .toList();
        booksByGenre.put(genre, sortedBooks);
    }
}
