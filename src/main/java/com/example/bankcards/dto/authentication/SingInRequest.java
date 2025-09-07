package com.example.bankcards.dto.authentication;

import jakarta.validation.constraints.NotBlank;

public record SingInRequest(
        @NotBlank(message = "Email обязателен")
        String email,
        @NotBlank(message = "Пароль обязателен")
        String password
) {
}
