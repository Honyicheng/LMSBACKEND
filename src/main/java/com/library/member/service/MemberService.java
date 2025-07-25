package com.library.member.service;

import com.library.common.entity.Loan;
import com.library.member.payload.ProfileUpdateRequest;

import java.util.List;

public interface MemberService {
    List<Loan> getMyLoans(String username);
    void returnBook(Long loanId);
    Loan extendLoan(Long loanId);
    void updateProfile(String username, ProfileUpdateRequest request);
    ProfileUpdateRequest getProfile(String name);
}
