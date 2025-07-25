package com.library.member.controller;

import com.library.common.entity.Book;
import com.library.common.entity.Loan;
import com.library.common.entity.User;
import com.library.common.repository.BookRepository;
import com.library.common.repository.LoanRepository;
import com.library.common.repository.UserRepository;
import com.library.member.service.MemberService;
import com.library.member.payload.ProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final UserRepository userRepo;
    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;

    @GetMapping("/loans")
    public List<Loan> getMyLoans(Principal principal) {
        return memberService.getMyLoans(principal.getName());
    }

    @PutMapping("/loans/{id}/return")
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        memberService.returnBook(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/loans/{id}/extend")
    public ResponseEntity<Loan> extendLoan(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.extendLoan(id));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest req, Principal principal) {
        memberService.updateProfile(principal.getName(), req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile/{name}")
    public ResponseEntity<?> getProfilebyName(@PathVariable String name) {
        return ResponseEntity.ok(memberService.getProfile(name));
    }


    @PostMapping("/loans")
    public ResponseEntity<String> borrowBook(@RequestParam Long bookId, Principal principal) {
        // 获取当前用户
        User user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 检查会员是否过期
        if (user.getMembershipDate().plusYears(1).isBefore(LocalDateTime.now())) {
            log.warn("User {} attempted to borrow a book with expired membership", user.getUsername());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Your membership has expired. Please renew before borrowing.");
        }

        // 获取当前借阅记录（未归还）
        List<Loan> activeLoans = loanRepo.findByUserAndReturnedFalse(user);

        // 检查当前借书数
        if (activeLoans.size() >= 3) {
            log.warn("User {} attempted to borrow more than 3 books", user.getUsername());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("You cannot borrow more than 3 books at the same time.");
        }

        // 检查是否有逾期
        boolean hasOverdue = activeLoans.stream()
                .anyMatch(loan -> loan.getDueDate().isBefore(LocalDate.now()));
        if (hasOverdue) {
            log.warn("User {} has overdue books", user.getUsername());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("You have overdue books. Please return them before borrowing new ones.");
        }

        // 查找书籍
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("This book is currently not available.");
        }

        // 创建借阅记录
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setBorrowDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14)); // 借阅期限 14 天
        loanRepo.save(loan);

        // 更新图书库存
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepo.save(book);

        log.info("User {} borrowed book {}", user.getUsername(), book.getTitle());
        return ResponseEntity.ok("Book borrowed successfully!");
    }


}
