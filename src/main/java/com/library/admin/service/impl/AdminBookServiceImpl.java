package com.library.admin.service.impl;

import com.library.admin.payload.BookRequest;
import com.library.admin.service.AdminBookService;
import com.library.common.entity.Book;
import com.library.common.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminBookServiceImpl implements AdminBookService {

    private final BookRepository bookRepo;

    @Override
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    @Override
    public Book addBook(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setCategory(request.getCategory());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getTotalCopies());
        return bookRepo.save(book);
    }

    @Override
    public Book updateBook(Long id, BookRequest request) {
        Book book = bookRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setCategory(request.getCategory());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getAvailableCopies());
        return bookRepo.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepo.deleteById(id);
    }
}
