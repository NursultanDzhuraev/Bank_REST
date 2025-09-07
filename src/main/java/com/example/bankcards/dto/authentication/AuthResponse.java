package com.example.bankcards.dto.authentication;

import com.example.bankcards.enums.Role;
import lombok.Builder;

@Builder
public record AuthResponse(
        Long id,
        String email,
        String token,
        Role role
) {
}
