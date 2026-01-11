package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry // Required for @Retryable [cite: 71]
public class DigitalBookshelfApplication {
    public static void main(String[] args) {
        SpringApplication.run(DigitalBookshelfApplication.class, args);
    }
}