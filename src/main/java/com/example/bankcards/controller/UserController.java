package com.example.bankcards.controller;

import com.example.bankcards.dto.PaginationResponse;
import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Secured("ADMIN")
    @Operation(summary = "Сохранить пользователя", description = "Доступен только для админа")
    @PostMapping("/save")
    public String saveUser(@RequestBody @Valid UserRequest userRequest) {
        return userService.saveUser(userRequest);
    }

    @Secured("ADMIN")
    @Operation(summary = "Найти всех пользователей", description = "Доступен только для админа")
    @GetMapping("/findAllUsers")
    public PaginationResponse<UserResponse> findAll(@RequestParam(defaultValue = "1") int pageNumber,
                                                    @RequestParam(defaultValue = "15") int pageSize) {
        return userService.findAll(pageNumber, pageSize);
    }

    @Secured("ADMIN")
    @Operation(summary = "Удалить user", description = "Только администратор может удалить user")
    @DeleteMapping("/deleteUserById/{userId}")
    public String deleteUserById(@PathVariable Long userId) {
        return userService.deleteUserById(userId);
    }

}
