package com.library.service;

import com.library.dto.BookRequest;
import com.library.model.Book;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository repository;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);



    private static final Random random = new SecureRandom();

    public Book addBookAsync(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setStatus("PROCESSING");

        repository.save(book);
        logger.info("New book received: {}", book.getTitle());

        executor.submit(() -> {
            try {

                Thread.sleep(3000);

                book.setStatus("AVAILABLE");

                logger.info("Book is now AVAILABLE: {}", book.getId());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        return book;
    }

    public List<Book> searchBooks(String author, String sort, int page, int size) {

        List<Book> result = repository.findAll();

        if (author != null && !author.isEmpty()) {
            result = result.stream()
                    .filter(b -> b.getAuthor().equalsIgnoreCase(author))
                    .collect(Collectors.toList());
        }

        if ("desc".equalsIgnoreCase(sort)) {
            result.sort(Comparator.comparing(Book::getTitle).reversed());
        } else {
            result.sort(Comparator.comparing(Book::getTitle));
        }

        int start = Math.min(page * size, result.size());
        int end = Math.min(start + size, result.size());
        return result.subList(start, end);
    }

    public Map<String, Long> getInventoryAudit() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(Book::getStatus, Collectors.counting()));
    }

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public String fetchBookCover(String id) throws Exception {
        logger.info("Connecting to Global Registry for book: {}", id);


        if (random.nextBoolean()) {
            throw new Exception("Network glitch!");
        }
        return "https://registry.com/covers/" + id + ".jpg";
    }

    public Optional<Book> findById(String id) {
        return repository.findById(id);
    }

    public boolean deleteBook(String id) {
        return repository.deleteById(id);
    }


    public Optional<Book> updateBook(String id, BookRequest request) {
        return repository.findById(id).map(book -> {
            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            return book;
        });
    }
}