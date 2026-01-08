package com.example.rest.service;

import com.example.rest.model.Book;
import com.example.rest.repository.BookRepository;
import com.example.rest.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookRepository bookRepository;

    // ---------------- GET ALL ----------------
    @Override
    public Page<Book> getBooks(String author, int page, int size, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by("title").descending()
                : Sort.by("title").ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (author != null && !author.isBlank()) {
            logger.info("Fetching books by author='{}'", author);
            return bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
        }

        logger.info("Fetching all books");
        return bookRepository.findAll(pageable);
    }

    // ---------------- GET BY ID ----------------
    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Book not found with id {}", id);
                    return new ResourceNotFoundException("Book", "id", id);
                });
    }

    // ---------------- CREATE (POST) ----------------
    @Override
    public Book createBook(Book book) {
        Book savedBook = bookRepository.save(book);
        logger.info("Created new book with id {}", savedBook.getId());
        return savedBook;
    }

    // ---------------- UPDATE (PUT) ----------------
    @Override
    public Book updateBook(Long id, Book book) {

        Book existingBook = getBookById(id); // reuse + exception safe

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());

        Book updatedBook = bookRepository.save(existingBook);
        logger.info("Updated book with id {}", id);
        return updatedBook;
    }

    // ---------------- DELETE ----------------
    @Override
    public void deleteBook(Long id) {

        Book existingBook = getBookById(id); // throws if not found
        bookRepository.delete(existingBook);

        logger.info("Deleted book with id {}", id);
    }
}
