package com.library.notification.service;


import com.library.common.entity.EmailLog;
import com.library.common.entity.Loan;
import com.library.common.entity.User;
import com.library.common.repository.EmailLogRepository;
import com.library.common.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepo;

    public void sendDueSoonEmail(Loan loan) {
        User user = loan.getUser();
        String to = user.getEmail();

        // 🛡️ 邮箱地址校验
        if (to == null || to.isEmpty()) {
            log.warn("❌ No email address for user: {}", user.getUsername());
            return;
        }

        String subject = "[Library Notice] Your Book is Due Soon";
        String content = String.format(
                "Dear %s,\n\nThis is a reminder that the following book is due in 7 days:\n\n" +
                        "📖 Title: %s\n📅 Due Date: %s\n\n" +
                        "Please return or renew the book on time to avoid late fees.\n\nBest regards,\nLibrary Management System",
                user.getUsername(),
                loan.getBook().getTitle(),
                loan.getDueDate()
        );

        try {
            // ✅ 发送邮件
            sendEmail(to, subject, content);
            // ✅ 记录邮件日志
            logEmail(user, subject, content);
        } catch (Exception e) {
            log.error("❌ Failed to send due-soon email to {}: {}", to, e.getMessage());
        }
    }


    public void sendOverdueEmail(Loan loan, long overdueDays) {
        User user = loan.getUser();
        String to = user.getEmail();

        // 🔒 校验邮箱地址
        if (to == null || to.isEmpty()) {
            log.warn("❌ No email address found for user: {}", user.getUsername());
            return;
        }

        // 💰 计算罚金（上限 $20）
        double fine = Math.min(overdueDays * 0.5, 20.0);

        String subject = "[Library Notice] Overdue Book and Fine Notice";
        String content = String.format(
                "Dear %s,\n\nYou have overdue books:\n\n" +
                        "📖 Title: %s\n📅 Due Date: %s\n💰 Overdue Days: %d\n🔻 Fine Amount: $%.2f\n\n" +
                        "Please return the book ASAP to stop the fine from increasing.\n\nBest regards,\nLibrary Management System",
                user.getUsername(),
                loan.getBook().getTitle(),
                loan.getDueDate(),
                overdueDays,
                fine
        );

        try {
            // ✅ 使用支持 HTML 的发送器
            sendEmail(to, subject, content);
            // 📝 记录发送日志
            logEmail(user, subject, content);
        } catch (Exception e) {
            log.error("❌ Failed to send overdue email to {}: {}", to, e.getMessage());
        }
    }


    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setFrom("honyicheng@gmail.com");
        mailSender.send(msg);
        log.info("✅ Email sent to {}: {}", to, subject);
    }



    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // 第二个参数 true 表示启用 HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private void logEmail(User user, String subject, String content) {
        EmailLog emailLoglog = new EmailLog();
        emailLoglog.setUser(user);
        emailLoglog.setMessage(subject + "\n\n" + content);
        emailLoglog.setTimestamp(LocalDateTime.now());
        try {
            // 保存日志
            emailLogRepo.save(emailLoglog);
        } catch (Exception e) {
            log.error("📛 Failed to save email log: {}", e.getMessage(), e);
        }

    }



//    public void sendAndLogEmail(String to, String subject, String htmlBody, String type) {
//        try {
//            // 1️⃣ 构建 MimeMessage 支持 HTML
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(htmlBody, true); // 第二个参数 true = 启用 HTML
//
//            // 2️⃣ 发送邮件
//            mailSender.send(mimeMessage);
//
//            // 3️⃣ 保存发送日志
//            userRepository.findByEmail(to).ifPresent(user -> {
//                EmailLog log = new EmailLog();
//                log.setMessage("[" + type + "] " + subject + " - " + htmlBody);
//                log.setTimestamp(LocalDateTime.now());
//                log.setUser(user);
//                emailLogRepository.save(log);
//            });
//
//        } catch (MessagingException e) {
//            throw new RuntimeException("Failed to send email", e);
//        }
//    }
}