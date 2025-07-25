package com.library.admin.service.impl;

import com.library.admin.payload.LoanDTO;
import com.library.admin.payload.LoanCreateRequest;
import com.library.admin.payload.LoanDTO;
import com.library.admin.service.AdminLoanService;
import com.library.common.entity.Book;
import com.library.common.entity.Loan;
import com.library.common.entity.User;
import com.library.common.repository.BookRepository;
import com.library.common.repository.LoanRepository;
import com.library.common.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;



@Service
@Transactional
@RequiredArgsConstructor
public class AdminLoanServiceImpl implements AdminLoanService {

    private final LoanRepository loanRepo;
    private final UserRepository userRepo;
    private final BookRepository bookRepo;

    @Override
    public List<Loan> getAllLoans() {
        return loanRepo.findAll();
    }

    public List<LoanDTO> getNAllLoans() {
        return loanRepo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private LoanDTO mapToDTO(Loan loan) {
        LoanDTO dto = new LoanDTO();
        dto.setId(loan.getId());
        dto.setUserid(loan.getUser().getId());
        dto.setUsername(loan.getUser().getUsername());
        dto.setBookTitle(loan.getBook().getTitle());
        dto.setBookId(loan.getBook().getId());
        dto.setBorrowDate(loan.getBorrowDate());
        dto.setDueDate(loan.getDueDate());
        dto.setReturned(loan.getReturned());
        dto.setReturnDate(loan.getReturnDate());
        dto.setFine(loan.getFine());
        return dto;
    }

    @Override
    public Loan createLoan(LoanCreateRequest request) {
        User user = userRepo.findById(request.getUserId()).orElseThrow();
        Book book = bookRepo.findByIsbn(request.getIsbn()).orElseThrow();

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No available copies");
        }

        if (loanRepo.countActiveLoansByUser(user.getId()) >= 3) {
            throw new RuntimeException("User already has 3 active loans");
        }

        if (loanRepo.hasOverdueLoans(user.getId())) {
            throw new RuntimeException("User has overdue loans");
        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setBorrowDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setReturned(false);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepo.save(book);
        return loanRepo.save(loan);
    }

    @Override
    public Loan extendLoan(Long loanId) {
        Loan loan = loanRepo.findById(loanId).orElseThrow();
        if (loan.getReturned()) throw new RuntimeException("Cannot extend returned loan");
        LocalDate newDueDate = loan.getDueDate().plusDays(7); // 假设每次延长7天
        loan.setDueDate(newDueDate);
        return loanRepo.save(loan);
    }

    @Override
    public void deleteLoan(Long id) {
        Loan loan = loanRepo.findById(id).orElseThrow();
        Book book = loan.getBook();
        if (!loan.getReturned()) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepo.save(book);
        }
        loanRepo.deleteById(id);
    }
}
