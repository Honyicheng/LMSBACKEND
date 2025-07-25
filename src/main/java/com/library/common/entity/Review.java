package com.library.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("user-review")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("book-review")
    private Book book;

    @Column(length = 1000)
    private String content;

    private int rating; // 1~5 星

    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;


}

