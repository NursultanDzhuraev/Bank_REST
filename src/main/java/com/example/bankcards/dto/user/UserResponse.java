package com.example.bankcards.dto.user;

import com.example.bankcards.entity.User;
import lombok.Builder;

import java.util.List;

@Builder
public class UserResponse {
    private Long userId;
    private String fullName;
    private String email;
    private int cardSize;

    public static UserResponse dtoToEntity(User user) {
        return   UserResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .cardSize(user.getCards().size())
                .build();
    }
    public static List<UserResponse> entityToDtoList(List<User> users) {
        return   users.stream().map(UserResponse::dtoToEntity).toList();
    }
}
