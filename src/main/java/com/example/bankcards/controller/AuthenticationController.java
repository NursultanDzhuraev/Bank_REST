package com.example.bankcards.controller;

import com.example.bankcards.dto.authentication.AuthResponse;
import com.example.bankcards.dto.authentication.SingInRequest;
import com.example.bankcards.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthService authService;

    @Operation(summary = "Вход", description = "Позволяет пользователю войти в систему")
    @PostMapping("/singIn")
    public AuthResponse singIn(@RequestBody @Valid SingInRequest singInRequest){
        return authService.singIn(singInRequest);
    }


}
