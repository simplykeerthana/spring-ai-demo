package com.example.springai.model;

import java.util.List;

/**
 * Book recommendation model for structured output
 */
public record BookRecommendation(
    String genre,
    List<Book> books
) {
    public record Book(
        String title,
        String author,
        int year,
        String description
    ) {}
}
