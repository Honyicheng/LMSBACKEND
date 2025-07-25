package com.library.member.review;

import com.library.common.entity.Book;
import com.library.common.entity.Review;
import com.library.common.entity.User;
import com.library.common.repository.BookRepository;
import com.library.common.repository.ReviewRepository;
import com.library.common.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/member/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewRepository reviewRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    @PostMapping("/{bookId}")
    @Transactional
    public void addReview(@PathVariable Long bookId,
                          @RequestBody Review review,
                          Principal principal) {
        User user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        // 判断是否已经评论过该书
//        if (reviewRepo.findByUserIdAndBookId(user.getId(), book.getId()).isPresent()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have already reviewed this book");
//        }


        review.setUser(user);
        review.setBook(book);
        review.setCreatedAt(LocalDate.now());

        // 验证评分范围
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        reviewRepo.save(review);
    }

    @GetMapping("/book/{bookId}")
    public List<Review> getReviews(@PathVariable Long bookId) {
        return reviewRepo.findByBookId(bookId);
    }
}

