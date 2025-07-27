package com.library.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReminderDto {

    private String email;
    private String bookTitle;
    private LocalDate dueDate;
    private boolean overdue;
    private double fine;
    private String message;
}