package com.library.admin.controller;


import com.library.admin.payload.LoanCreateRequest;
import com.library.admin.payload.LoanDTO;
import com.library.admin.service.AdminLoanService;
import com.library.common.entity.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/loans")
@RequiredArgsConstructor
public class AdminLoanController {

    private final AdminLoanService loanService;
//
//    @GetMapping
//    public List<Loan> getAllLoans() {
//        return loanService.getAllLoans();
//    }

    @GetMapping
    public List<LoanDTO> getAllLoans() {
        return loanService.getNAllLoans();
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody LoanCreateRequest request) {
        return ResponseEntity.ok(loanService.createLoan(request));
    }

    @PutMapping("/{id}/extend")
    public ResponseEntity<Loan> extendLoan(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.extendLoan(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.ok().build();
    }
}
