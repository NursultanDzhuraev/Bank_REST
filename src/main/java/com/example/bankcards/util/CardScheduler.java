package com.example.bankcards.util;

import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.repository.CardRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@EnableScheduling
public class CardScheduler {
    private final CardRepository cardRepository;

    public CardScheduler(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void expireOldCards() {
        List<Card> cards = cardRepository.findAll();
        cards.stream()
                .filter(c -> c.getExpirationDate().isBefore(LocalDate.now()))
                .forEach(c -> c.setStatus(CardStatus.EXPIRED));
        cardRepository.saveAll(cards);
    }
}
