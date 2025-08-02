package com.library.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table
@Data
public class CheckoutRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "member_id" ,nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id" ,nullable = false)
    private Book book;


    @Column(nullable = false)
    private LocalDate checkoutDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;
    // if returnDate = null, the book is not returned

    private Double overdueFine;


}
