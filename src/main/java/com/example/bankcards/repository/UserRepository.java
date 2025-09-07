package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.NotFoundException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    default User findByEmailOrElseThrow(@NotBlank(message = "Email обязателен") String email){
        return findByEmail(email)
                .orElseThrow(()-> new NotFoundException("Пользователь с email " + email + " не найден"));
    }

    default User findByIdOrElseThrow(Long userId){
        return findById(userId)
                .orElseThrow(()-> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }

    @Query("select u from User u where u.role = 'USER'")
    Page<User> findAllUsers(Pageable pageable);
}
