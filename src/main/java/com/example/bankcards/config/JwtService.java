package com.example.bankcards.config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.Instant;


@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;

    @Value("${spring.security.jwt.secret}")
    private String SECRET;

    public String generateToken(User user) {
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("email", user.getEmail());
        builder.withClaim("id", user.getId());
        builder.withClaim("role", user.getRole().getAuthority());
        builder.withIssuedAt(Instant.now());
        builder.withExpiresAt(Instant.now().plusSeconds(36000));
        return builder.sign(Algorithm.HMAC256(SECRET));
    }

    public User verifyToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
        String email = decodedJWT.getClaim("email").asString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

}