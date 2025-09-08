package com.example.bankcards.service.impl;

import com.example.bankcards.config.JwtService;
import com.example.bankcards.dto.authentication.AuthResponse;
import com.example.bankcards.dto.authentication.SingInRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.IllegalArgumentException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
 private final UserRepository userRepository;
 private final PasswordEncoder passwordEncoder;
 private final JwtService jwtService;

    @Override
    public AuthResponse singIn(SingInRequest singInRequest) {
        User user = userRepository.findByEmailOrElseThrow(singInRequest.email());
        if(!passwordEncoder.matches(singInRequest.password(),user.getPassword())){
            throw new IllegalArgumentException("Неправильный пароль");
        }
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }
}
