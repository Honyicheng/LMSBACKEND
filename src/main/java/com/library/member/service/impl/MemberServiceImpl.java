package com.library.member.service.impl;

import com.library.common.entity.Book;
import com.library.common.entity.Loan;
import com.library.common.entity.User;
import com.library.common.repository.BookRepository;
import com.library.common.repository.LoanRepository;
import com.library.common.repository.UserRepository;
import com.library.member.payload.ProfileUpdateRequest;
import com.library.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final LoanRepository loanRepo;
    private final UserRepository userRepo;
    private final BookRepository bookRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Loan> getMyLoans(String username) {
        User user = userRepo.findByUsername(username).orElseThrow();
        return loanRepo.findByUserId(user.getId());
    }

    @Override
    public void returnBook(Long loanId) {
        Loan loan = loanRepo.findById(loanId).orElseThrow();
        if (!loan.getReturned()) {
            loan.setReturned(true);
            loan.setReturnDate(LocalDate.now());

            long daysLate = LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay();
            if (daysLate > 0) {
                double fine = Math.min(daysLate * 0.5, 20.0);
                loan.setFine(fine);
            }

            Book book = loan.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepo.save(book);
            loanRepo.save(loan);
        }
    }

    @Override
    public Loan extendLoan(Long loanId) {
        Loan loan = loanRepo.findById(loanId).orElseThrow();
        if (loan.getReturned()) throw new RuntimeException("Loan already returned");
        loan.setDueDate(loan.getDueDate().plusDays(7)); // 续借 7 天
        return loanRepo.save(loan);
    }

    @Override
    public void updateProfile(String username, ProfileUpdateRequest request) {
        User user = userRepo.findByUsername(username).orElseThrow();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
//        user.setPassword(request.getPassword()); //
        user.setPassword(passwordEncoder.encode(request.getPassword()));; // 建议加密
        userRepo.save(user);
    }

    @Override
    public ProfileUpdateRequest getProfile(String name) {
        User user= userRepo.findByUsername(name).orElseThrow();
        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setUsername(user.getUsername());
        request.setEmail(user.getEmail());
        request.setPassword(user.getPassword()); // 建议加密
        return request;
    }


}
