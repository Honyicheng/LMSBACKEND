package com.library.common.repository;
//
//import com.library.common.entity.Book;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface BookRepository extends JpaRepository<Book, Long> {
//    Optional<Book> findByIsbn(String isbn);
//
//    // Available books with quantity > 0
//    @Query("SELECT b FROM Book b WHERE b.availableCopies > 0")
//    List<Book> findAvailableBooks();
//
//    // Search with pagination
//    @Query("SELECT b FROM Book b WHERE " +
//            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    Page<Book> searchBooks(@Param("keyword") String keyword, Pageable pageable);
//
//    // Update availableCopies
//    @Modifying
//    @Transactional
//    @Query("UPDATE Book b SET b.availableCopies = b.availableCopies + :change WHERE b.id = :bookId")
//    void updateavailableCopies(@Param("bookId") Long bookId, @Param("change") int change);
//
//    // Check availability
//    @Query("SELECT CASE WHEN b.availableCopies > 0 THEN true ELSE false END FROM Book b WHERE b.id = :bookId")
//    boolean isBookAvailable(@Param("bookId") Long bookId);
//
//    // Find books by author with pagination
//    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
//}


import com.library.common.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // 查找指定 ISBN 的图书
    Optional<Book> findByIsbn(String isbn);

    // 所有可借图书
    @Query("SELECT b FROM Book b WHERE b.availableCopies > 0")
    List<Book> findAvailableBooks();

    // 模糊搜索（标题、作者、ISBN、分类），支持分页
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "CAST(b.totalCopies AS string) LIKE CONCAT('%', :keyword, '%') OR " +
            "LOWER(b.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Book> searchBooks(@Param("keyword") String keyword, Pageable pageable);

    // 增加/减少 availableCopies 数量
    @Modifying
    @Transactional
    @Query("UPDATE Book b SET b.availableCopies = b.availableCopies + :delta WHERE b.id = :bookId")
    void updateAvailableCopies(@Param("bookId") Long bookId, @Param("delta") int delta);

    // 检查是否可借
    @Query("SELECT CASE WHEN b.availableCopies > 0 THEN true ELSE false END FROM Book b WHERE b.id = :bookId")
    boolean isAvailable(@Param("bookId") Long bookId);

    // 按分类分页查询
    Page<Book> findByCategoryIgnoreCase(String category, Pageable pageable);

    // 按作者模糊搜索
    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
}
