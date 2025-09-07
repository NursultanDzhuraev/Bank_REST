package com.example.bankcards.dto.card;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CardRequest(
        @NotNull(message = "Поле cardNumber не должен быть пустым")
        @Pattern(regexp = "\\d{16}", message = "Номер карты должен состоять из 16 цифр")
        String cardNumber,
        @NotNull(message = "Поле expirationDate не должен быть пустым")
        @FutureOrPresent(message = "Срок действия карты должен истекать не ранее сегодняшнего дня")
        LocalDate expirationDate
) {
}
