package com.library.nomember.controller;

import com.library.common.entity.Book;
import com.library.common.entity.Review;
import com.library.common.repository.BookRepository;
import com.library.common.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Slf4j
public class PublicBookController {

    private final BookRepository bookRepo;
    private final ReviewRepository reviewRepo;

    @GetMapping("/books{bookId}/reviews")
    public List<Review> getReviewsbybookid(@PathVariable("bookId") Long bookId) {
        if (bookId == null) {
            throw new IllegalArgumentException("Book ID must not be null");
        }
        Book book = bookRepo.findById(bookId).orElseThrow();
        log.info("Getting reviews for book with ID: {}", bookId);

        return reviewRepo.findByBookId(bookId);
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    @GetMapping("/books/{id}")
    public Book getBookDetail(@PathVariable Long id) {
        return bookRepo.findById(id).orElseThrow();
    }
}
