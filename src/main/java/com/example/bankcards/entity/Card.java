package com.example.bankcards.entity;

import com.example.bankcards.enums.CardStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cards")
@Getter
@Setter
public class Card {
    @Id
    @GeneratedValue(generator = "card_gen",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "card_gen", sequenceName = "card_seq" , allocationSize = 1, initialValue = 100)
    private Long id;
    @Column(nullable = false, unique = true)
    private String cardNumber;
    @Column(nullable = false)
    private LocalDate expirationDate;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private CardStatus status;
    @Column(unique = true, nullable = false)
    private String cardHash;
    private String requestCardStatus;

    @ManyToOne
    private User user;
}
