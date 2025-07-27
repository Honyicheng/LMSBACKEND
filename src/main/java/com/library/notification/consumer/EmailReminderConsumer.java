package com.library.notification.consumer;

import com.library.common.entity.Loan;
import com.library.common.entity.User;
import com.library.common.repository.LoanRepository;

import com.library.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailReminderConsumer {

    private final LoanRepository loanRepo;
    private final EmailService emailService;


    @KafkaListener(topics = "email-reminder-topic", groupId = "lms-group")
    public void handleEmailReminder(String trigger) {
        try {
            List<Loan> loans = loanRepo.findByReturnedFalse();
            for (Loan loan : loans) {
                User user = loan.getUser();
                if (user == null || user.getEmail() == null) continue;

                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), loan.getDueDate());
                long overdueDays = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());

                if (daysLeft == 7) {
                    emailService.sendDueSoonEmail(loan);
                }

                if (overdueDays > 0) {
                    emailService.sendOverdueEmail(loan, overdueDays);
                }
            }
        } catch (Exception ex) {
            log.error("❌ Error handling email reminder Kafka message: {}", ex.getMessage(), ex);
        }
    }

}