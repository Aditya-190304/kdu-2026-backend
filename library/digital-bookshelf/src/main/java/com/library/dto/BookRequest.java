package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookRequest {
    @NotBlank(message = "Title cannot be empty") //
    @Size(min = 2, message = "Title must be at least 2 characters") //
    private String title;

    @NotBlank(message = "Author cannot be empty")
    private String author;
}