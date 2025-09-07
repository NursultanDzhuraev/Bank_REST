package com.example.bankcards.service;

import com.example.bankcards.dto.PaginationResponse;
import com.example.bankcards.dto.card.CardRequest;
import com.example.bankcards.dto.card.CardResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CardService {
    ResponseEntity<?> saveCard(@Valid CardRequest cardRequest, Long userId);

    String deleteUserById(Long cardId);

    PaginationResponse<CardResponse> findAll(int pageNumber, int pageSize);

    List<CardResponse> findUserCardsForAdmin(Long userId);

    List<CardResponse> findUserCards();

    String blocksOrActivatesCard(Long cardId);
}
