package com.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private String id = UUID.randomUUID().toString();
    private String title;
    private String author;
    private String status;
}