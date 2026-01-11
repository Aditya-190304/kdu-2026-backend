package com.library.controller;

import com.library.dto.BookRequest;
import com.library.model.Book;
import com.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Library API", description = "Management endpoints") // Swagger
public class BookController {

    private final BookService bookService;


    @PostMapping
    @Operation(summary = "Add a new book", description = "Starts async processing. Returns 202 Accepted.") // [cite: 152]
    public ResponseEntity<EntityModel<Book>> addBook(@Valid @RequestBody BookRequest request) {
        Book savedBook = bookService.addBookAsync(request);


        return ResponseEntity.accepted().body(addLinks(savedBook));
    }


    @GetMapping
    @Operation(summary = "Get all books", description = "Supports filtering by author and sorting")
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.searchBooks(author, sort, page, size));
    }


    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Book>> getBookById(@PathVariable String id) {
        return bookService.findById(id)
                .map(book -> ResponseEntity.ok(addLinks(book)))
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Book>> updateBook(@PathVariable String id, @Valid @RequestBody BookRequest request) {
        return bookService.updateBook(id, request)
                .map(book -> ResponseEntity.ok(addLinks(book)))
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        if (bookService.deleteBook(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/analytics/audit")
    public ResponseEntity<Map<String, Long>> getAudit() {
        return ResponseEntity.ok(bookService.getInventoryAudit());
    }


    private EntityModel<Book> addLinks(Book book) {
        EntityModel<Book> model = EntityModel.of(book);
        model.add(linkTo(methodOn(BookController.class).getBookById(book.getId())).withSelfRel()); // [cite: 50]
        model.add(linkTo(methodOn(BookController.class).getAllBooks(null, "asc", 0, 10)).withRel("all-books")); // [cite: 51]
        return model;
    }
}