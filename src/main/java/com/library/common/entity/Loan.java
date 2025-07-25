package com.library.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Loan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
//    @JsonManagedReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
//    @JsonManagedReference
    private Book book;

    private LocalDate borrowDate;
    private LocalDate dueDate;

    private Boolean returned = false;

    private LocalDate returnDate;
    private Double fine = 0.0;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;
}

