package com.example.rest.repository;

import com.example.rest.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
}

