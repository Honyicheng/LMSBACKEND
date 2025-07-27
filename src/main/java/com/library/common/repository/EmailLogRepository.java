package com.library.common.repository;

import com.library.common.entity.EmailLog;
import com.library.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
    Page<EmailLog> findAll(Pageable pageable);
    Page<EmailLog> findByUser(User user, Pageable pageable);

    List<EmailLog> findByUser(User user);
}