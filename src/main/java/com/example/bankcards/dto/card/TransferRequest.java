package com.example.bankcards.dto.card;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "не может быть null")
        Long fromCardId,
        @NotNull(message = "не может быть null")
        Long toCardId,
        @NotNull(message = "не может быть null")
        @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0")
        @DecimalMax(value = "1000000", message = "Сумма не может превышать 1 000 000")
        BigDecimal amount,
        String description
) {
}
