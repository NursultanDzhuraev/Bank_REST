package com.example.bankcards.service.impl;

import com.example.bankcards.dto.PaginationResponse;
import com.example.bankcards.dto.card.CardRequest;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.card.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.enums.Role;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.exception.IllegalArgumentException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.EncryptionUtil;
import com.example.bankcards.util.HashUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;

    @Override
    @Transactional
    public ResponseEntity<?> saveCard(CardRequest cardRequest, Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        if (user.getRole().equals(Role.ADMIN)) {
            throw new IllegalArgumentException("Не может для админа создавать карту");
        }
        String encryptCardNumber = encryptionUtil.encrypt(cardRequest.cardNumber());
        String cardHash = HashUtil.sha256(cardRequest.cardNumber());
        Optional<Card> findCard = cardRepository.findByCardHash(cardHash);
        if (findCard.isPresent()) {
            throw new BadRequestException("Карта с таким номером уже существует");
        }
        Card card = new Card();
        card.setCardNumber(encryptCardNumber);
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.ZERO);
        card.setExpirationDate(cardRequest.expirationDate());
        card.setCardHash(cardHash);
        card.setUser(user);
        cardRepository.save(card);
        return ResponseEntity.status(HttpStatus.CREATED).body("Успешно сохранено");
    }

    @Override
    public String deleteUserById(Long cardId) {
        Card card = cardRepository.findByIdOrElseThrow(cardId);
        card.setUser(null);
        cardRepository.delete(card);
        return "успешно удалено";
    }

    @Override
    public PaginationResponse<CardResponse> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Card> allCard = cardRepository.findAll(pageable);
        var response = new PaginationResponse<CardResponse>();
        response.setPageNumber(allCard.getNumber() + 1);
        response.setPageSize(allCard.getSize());
        response.setTotalPages(allCard.getTotalPages());
        response.setTotalElements(allCard.getTotalElements());
        response.setContent(allCard
                .stream()
                .map(card -> CardResponse.fromCard(card, encryptionUtil))
                .collect(Collectors.toList()));
        return response;
    }

    @Override
    public List<CardResponse> findUserCardsForAdmin(Long userId) {
        return findByUserId(userId);
    }

    @Override
    public List<CardResponse> findUserCards() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmailOrElseThrow(email);
        return findByUserId(user.getId());
    }

    @Override
    public String blocksOrActivatesCard(Long cardId) {
        Card card = cardRepository.findByIdOrElseThrow(cardId);
        if (card.getStatus() == CardStatus.EXPIRED) {
            throw new BadRequestException("Истек срок карту");
        }
        String status = null;
        if (card.getStatus() == CardStatus.ACTIVE) {
            card.setStatus(CardStatus.BLOCKED);
            status = "blocked";
        } else if (card.getStatus() == CardStatus.BLOCKED) {
            card.setStatus(CardStatus.ACTIVE);
            status = "active";
        }
        cardRepository.save(card);
        return "Успешно статус карту на " + status;
    }

    @Override
    @Transactional
    public ResponseEntity<?> transferBetweenCards(TransferRequest transferRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmailOrElseThrow(email);
        Card fromCard = cardRepository.findByIdOrElseThrow(transferRequest.fromCardId());
        Card toCard = cardRepository.findByIdOrElseThrow(transferRequest.toCardId());
        if (!fromCard.getUser().getId().equals(user.getId()) ||
                !toCard.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Переводить можно только между своими картами");
        }
        validateCardForTransfer(fromCard);
        validateCardForTransfer(toCard);
        if (fromCard.getBalance().compareTo(transferRequest.amount()) < 0) {
            throw new IllegalStateException("Недостаточный баланс");
        }
        fromCard.setBalance(fromCard.getBalance().subtract(transferRequest.amount()));
        toCard.setBalance(toCard.getBalance().add(transferRequest.amount()));
        cardRepository.save(fromCard);
        cardRepository.save(toCard);
        return ResponseEntity.status(HttpStatus.OK).body("Перевод успешно завершен");
    }

    private void validateCardForTransfer(Card card) {
        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new IllegalStateException(" карта не активна для переводов");
        }

        if (card.getExpirationDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException(" срок действия карты истек");
        }
    }

    private List<CardResponse> findByUserId(Long userId) {
        List<Card> cardList = cardRepository.findByUserId(userId);
        List<CardResponse> response = new ArrayList<>();
        for (Card card : cardList) {
            response.add(CardResponse.fromCard(card, encryptionUtil));
        }
        return response;
    }

}
