package com.example.bankcards.controller;

import com.example.bankcards.dto.PaginationResponse;
import com.example.bankcards.dto.card.CardRequest;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;


    @Secured("ADMIN")
    @Operation(summary = "сохранить карту", description = "Доступен только для админа")
    @PostMapping("/save/{userId}")
    public ResponseEntity<?> saveCard(@RequestBody @Valid CardRequest cardRequest,
                                      @PathVariable("userId") Long userId) {
        return cardService.saveCard(cardRequest, userId);
    }

    @Secured("ADMIN")
    @Operation(summary = "Удалить карту", description = "Только администратор может удалить карту")
    @DeleteMapping("/deleteCardById/{cardId}")
    public String deleteCardById(@PathVariable Long cardId) {
        return cardService.deleteUserById(cardId);
    }

    @Secured("ADMIN")
    @Operation(summary = "Найти всех карту", description = "Доступен только для админа")
    @GetMapping("/findAllCards")
    public PaginationResponse<CardResponse> findAll(@RequestParam(defaultValue = "1") int pageNumber,
                                                    @RequestParam(defaultValue = "15") int pageSize) {
        return cardService.findAll(pageNumber, pageSize);
    }
    @Secured({"ADMIN"})
    @Operation(summary = "Найти все карту пользователя", description = "Доступен только для админа")
    @GetMapping("/findUserCardsForAdmin/{userId}")
    public List<CardResponse> findUserCardsForAdmin(@PathVariable Long userId) {
        return cardService.findUserCardsForAdmin(userId);
    }
    @Secured({"USER"})
    @Operation(summary = "Найти все карту пользователя", description = "Доступен только для пользователя")
    @GetMapping("/findUserCards")
    public List<CardResponse> findUserCards() {
        return cardService.findUserCards();
    }
    @Secured("ADMIN")
    @Operation(summary = "блокирует или активирует карту", description = "Доступен только для админа")
    @PostMapping("/blocksOrActivatesCard/{cardId}")
    public String blocksOrActivatesCard(@PathVariable Long cardId) {
        return cardService.blocksOrActivatesCard(cardId);
    }
}
