package com.martishyn.week1.src;

import com.martishyn.week1.src.model.Book;
import com.martishyn.week1.src.model.Genre;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class LibraryPractice {

    public static void main(String[] args) {
        printUniqueAuthorsSorted(library);
        printAuthorBookCounts(library);
        printEmptyGenres(library);
        Set<Book> allUniqueBooks = getAllUniqueBooks(library);
        allUniqueBooks.forEach(System.out::println);
    }

    private static final Library library = new Library();

    static {
        library.addBook(new Book("Book1", "Asimov", Genre.BIOGRAPHY));
        library.addBook(new Book("Book2", "Asimov", Genre.BIOGRAPHY));
        library.addBook(new Book("Book3", "Asimov", Genre.BIOGRAPHY));
        library.addBook(new Book("Book4", "King", Genre.SCIFI));
        library.addBook(new Book("Book5", "Tolkien", Genre.BIOGRAPHY));
        library.addBook(new Book("Book6", "Tolkien", Genre.BIOGRAPHY));

    }

    /**
     * Виведи унікальні імена авторів, які є в бібліотеці.
     * Відсортуй їх у алфавітному порядку.
     */
    public static void printUniqueAuthorsSorted(Library library) {
        final LinkedHashSet<String> list = library.getAllBooks()
                .stream()
                .flatMap(s -> s.stream().map(Book::getAuthor))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        final TreeSet<String> list1 = library.getAllBooks()
                .stream()
                .flatMap(s -> s.stream().map(Book::getAuthor))
                .collect(Collectors.toCollection(TreeSet::new));

        list.forEach(System.out::println);
        list1.forEach(System.out::println);
    }

    /**
     * Порахуйте скільки книг кожен автор має в бібліотеці.
     * Виведіть у форматі:
     * Tolkien -> 3
     * King -> 2
     */
    public static void printAuthorBookCounts(Library library) {
        final Map<String, Long> collect = library.getAllBooks()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()));
        for (var entry : collect.entrySet()) {
            System.out.printf("%s -> %s\n", entry.getKey(), entry.getValue());
        }
    }

    /**
     * Виведіть жанри, в яких немає жодної книги.
     */
    public static void printEmptyGenres(Library library) {
        final Map<Genre, List<Book>> booksCollection = library.getBooksCollection();
        final List<Genre> genresWithoutBooks = new ArrayList<>();
        final Genre[] values = Genre.values();
        for (var genre : values) {
            if (booksCollection.get(genre) == null) {
                genresWithoutBooks.add(genre);
            }
        }
        genresWithoutBooks.forEach(gen -> System.out.printf("%s \n", gen.name()));
    }

    /**
     * Поверни список усіх книг без дублів (Set).
     */
    public static Set<Book> getAllUniqueBooks(Library library) {
        return library.getAllBooks()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static List<Book> sortBooksByTitleLength(Library library) {
        // Напиши stream, який повертає книги в порядку зростання довжини назви
        return library.getAllBooks()
                .stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Book::getTitle))
                .toList();
    }

    public static Map<Genre, List<Book>> groupBooksByGenre(Library library) {
//        // Використай Collectors.groupingBy
        return library.getAllBooks()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Book::getGenre, toList()));
    }

    public static List<Book> findBooksByAuthor(Library library, String author) {
        // Stream -> filter by author -> toList
        return library.getAllBooks()
                .stream()
                .flatMap(List::stream)
                .filter(b -> b.getAuthor().equalsIgnoreCase(author))
                .toList();
    }

    public static Map<Genre, Long> countBooksPerGenre(Library library) {
        return library.getAllBooks()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Book::getGenre,  Collectors.counting()));
    }

    public static Optional<Book> findBookWithLongestTitle(Library library) {
        return library.getAllBooks()
                .stream()
                .flatMap(List::stream)
                .max(Comparator.comparingInt(b -> b.getTitle().length()));
    }





}
