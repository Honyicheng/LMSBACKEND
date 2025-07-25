package com.library.admin.service;

import com.library.admin.payload.BookRequest;
import com.library.common.entity.Book;

import java.util.List;

public interface AdminBookService {
    List<Book> getAllBooks();
    Book addBook(BookRequest request);
    Book updateBook(Long id, BookRequest request);
    void deleteBook(Long id);
}
