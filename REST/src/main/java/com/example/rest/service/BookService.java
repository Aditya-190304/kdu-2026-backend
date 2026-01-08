package com.example.rest.service;

import com.example.rest.model.Book;
import com.example.rest.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;

public interface BookService {

    // GET all (filter + sort + pagination)
    Page<Book> getBooks(String author, int page, int size, String sortDir);

    // GET by id
    Book getBookById(Long id) throws ResourceNotFoundException;

    // POST (create)
    Book createBook(Book book);

    // PUT (update)
    Book updateBook(Long id, Book book) throws ResourceNotFoundException;

    // DELETE
    void deleteBook(Long id) throws ResourceNotFoundException;
}
