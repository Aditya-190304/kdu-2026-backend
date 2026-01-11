package com.library.repository;

import com.library.model.Book;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BookRepository {

    private final List<Book> db = Collections.synchronizedList(new ArrayList<>());

    public Book save(Book book) {
        db.add(book);
        return book;
    }

    public List<Book> findAll() {

        synchronized (db) {
            return new ArrayList<>(db);
        }
    }

    public Optional<Book> findById(String id) {

        synchronized (db) {
            return db.stream()
                    .filter(b -> b.getId().equals(id))
                    .findFirst();
        }
    }

    public boolean deleteById(String id) {

        return db.removeIf(b -> b.getId().equals(id));
    }
}