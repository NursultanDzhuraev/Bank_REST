package com.example.bankcards.dto.user;


import com.example.bankcards.validation.email.ValidEmail;
import com.example.bankcards.validation.password.ValidPassword;

public record UserRequest(
        @ValidEmail
        String email,
        @ValidPassword
        String password,
        String fullName
) {
}
