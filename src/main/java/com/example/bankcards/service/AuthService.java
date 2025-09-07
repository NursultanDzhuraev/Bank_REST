package com.example.bankcards.service;

import com.example.bankcards.dto.authentication.AuthResponse;
import com.example.bankcards.dto.authentication.SingInRequest;
import jakarta.validation.Valid;

public interface AuthService {
    AuthResponse singIn(@Valid SingInRequest singInRequest);

}
