package com.example.bankcards.service.impl;

import com.example.bankcards.dto.PaginationResponse;
import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.Role;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardService cardService;

    @Override
    public String saveUser(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.email()).isPresent()) {
            throw new BadRequestException("Пользователь с таким email уже существует");
        }
        User user = new User();
        user.setEmail(userRequest.email());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setRole(Role.USER);
        user.setFullName(userRequest.fullName());
        userRepository.save(user);
        return "Успешно сохранен";
    }

    @Override
    public PaginationResponse<UserResponse> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<User> allUsers = userRepository.findAllUsers(pageable);
        var response = new PaginationResponse<UserResponse>();
        response.setPageNumber(allUsers.getNumber() + 1);
        response.setPageSize(allUsers.getSize());
        response.setTotalPages(allUsers.getTotalPages());
        response.setTotalElements(allUsers.getTotalElements());
        response.setContent(UserResponse.entityToDtoList(allUsers.getContent()));
        return response;
    }

    @Override
    public String deleteUserById(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        List<Card> cards = user.getCards();
        for (Card card : cards) {
           cardService.deleteUserById(card.getId());
        }
      userRepository.deleteById(userId);
        return "User успешно удалено";
    }

}
