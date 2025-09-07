package com.example.bankcards.service;

import com.example.bankcards.dto.PaginationResponse;
import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.dto.user.UserResponse;
import jakarta.validation.Valid;

public interface UserService {

    String saveUser(@Valid UserRequest userRequest);

    PaginationResponse<UserResponse> findAll(int pageNumber, int pageSize);

    String deleteUserById(Long userId);

}
