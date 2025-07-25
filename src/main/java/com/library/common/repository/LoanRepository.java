package com.library.common.repository;

import com.library.common.entity.Loan;
import com.library.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.user.id = :userId AND l.returned = false")
    int countActiveLoansByUser(Long userId);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END " +
            "FROM Loan l WHERE l.user.id = :userId AND l.returned = false AND l.dueDate < CURRENT_DATE")
    boolean hasOverdueLoans(Long userId);

    List<Loan> findByUserId(Long userId);

    List<Loan> findByUserAndReturnedFalse(User user);

//    List<Loan> findByUserAndReturnedAtIsNull(User user);
}
