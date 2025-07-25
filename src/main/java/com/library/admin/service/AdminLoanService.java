package com.library.admin.service;


import com.library.admin.payload.LoanCreateRequest;
import com.library.admin.payload.LoanDTO;
import com.library.common.entity.Loan;

import java.util.List;

public interface AdminLoanService {
    List<Loan> getAllLoans();
    List<LoanDTO> getNAllLoans();
    Loan createLoan(LoanCreateRequest request);
    Loan extendLoan(Long loanId);
    void deleteLoan(Long id);
}
