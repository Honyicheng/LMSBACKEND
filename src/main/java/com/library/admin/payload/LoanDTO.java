package com.library.admin.payload;

import lombok.Data;

import java.time.LocalDate;

@Data
    public class LoanDTO {
        private Long id;
        private Long userid;
        private String username;
        private LocalDate borrowDate;
        private LocalDate dueDate;
        private LocalDate returnDate;
        private boolean returned;
        private double fine;
        private Long bookId;         // 👈 新增
        private String bookTitle;    // 现有字段
        private String memberName;   // 现有字段
    }

