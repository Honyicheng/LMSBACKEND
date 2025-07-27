package com.library.member.service;

import com.library.common.entity.EmailLog;
import com.library.common.entity.ReminderDto;
import com.library.common.entity.User;
import com.library.common.repository.EmailLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailReminderService {

    private final EmailLogRepository emailLogRepository;

    public List<ReminderDto> getRemindersForUser(User user) {
        return emailLogRepository.findByUser(user).stream()
                .map(log -> {
                    ReminderDto dto = new ReminderDto();
                    dto.setMessage(log.getMessage());
                    dto.setEmail(log.getUser().getEmail());
                    dto.setBookTitle(log.getUser().getUsername());
                    return dto;
                })
                .toList();
    }

    public Page<EmailLog> getAllEmailLogs(Pageable pageable) {
        return emailLogRepository.findAll(pageable);
    }
}

