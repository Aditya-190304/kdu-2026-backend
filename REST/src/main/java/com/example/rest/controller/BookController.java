package com.example.rest.controller;

import com.example.rest.model.Book;
import com.example.rest.service.BookService;
import com.example.rest.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    // ----------------- GET ALL BOOKS -----------------
    @GetMapping
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        logger.info("GET /books called with author={}, page={}, size={}, sortDir={}", author, page, size, sortDir);
        return ResponseEntity.ok(bookService.getBooks(author, page, size, sortDir).getContent());
    }

    // ----------------- GET BOOK BY ID -----------------
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) throws ResourceNotFoundException {
        Book book = bookService.getBookById(id);
        logger.info("GET /books/{} successful", id);
        return ResponseEntity.ok(book);
    }

    // ----------------- ADD NEW BOOK -----------------
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.status(201).body(createdBook);
    }

    // ----------------- UPDATE BOOK -----------------
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody Book bookDetails
    ) throws ResourceNotFoundException {
        Book updatedBook = bookService.updateBook(id, bookDetails);
        logger.info("PUT /books/{} - Book updated", id);
        return ResponseEntity.ok(updatedBook);
    }

    // ----------------- DELETE BOOK -----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) throws ResourceNotFoundException {
        bookService.deleteBook(id);
        logger.info("DELETE /books/{} - Book deleted", id);
        return ResponseEntity.noContent().build();
    }
}
