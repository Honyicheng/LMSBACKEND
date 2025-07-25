package com.library.admin.payload;

import lombok.Data;

@Data
public class BookRequest {
    private String title;
    private String author;
    private String isbn;
    private String category;
    private int totalCopies;
    private int availableCopies;
}
