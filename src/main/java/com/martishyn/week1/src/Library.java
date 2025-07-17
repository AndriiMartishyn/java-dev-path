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

    public Map<Genre, List<Book>> getBooksCollection(){
        return booksByGenre;
    }

    public Collection<List<Book>> getAllBooks(){
        return booksByGenre.values();
    }

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException();
        }
        booksByGenre.computeIfAbsent(book.getGenre(), key -> new ArrayList<>()).add(book);
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
