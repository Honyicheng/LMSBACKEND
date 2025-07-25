package com.library.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String author;
    private String isbn;
    private String category;

    private int totalCopies;
    private int availableCopies;
}
