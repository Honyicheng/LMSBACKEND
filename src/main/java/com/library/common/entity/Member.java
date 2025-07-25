//package com.library.common.entity;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "members")
//public class Member {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String firstName;
//    private String lastName;
//    private String email;
//    private String phone;
//    private String address;
//
//
//    private LocalDate membershipStart;
//    private LocalDate membershipEnd;
//
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Loan> loans = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Review> reviews = new ArrayList<>();
//
//    // Constructors, getters, setters
//}