package com.library.admin.controller;

import com.library.common.entity.EmailLog;
import com.library.common.repository.EmailLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/email-logs")
@RequiredArgsConstructor
public class AdminEmailLogController {

    private final EmailLogRepository emailLogRepo;

    @GetMapping
    public Page<EmailLog> getEmailLogs(@RequestParam(defaultValue = "0") int page) {
        return emailLogRepo.findAll(PageRequest.of(page, 10));
    }
}