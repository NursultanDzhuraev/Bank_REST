package com.example.bankcards.dto.card;

import com.example.bankcards.entity.Card;
import com.example.bankcards.util.EncryptionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CardResponse {
    private String cardNumber;
    private BigDecimal balance;
    private LocalDate expirationDate;
    private String owner;
    private String cardStatus;

    public static CardResponse fromCard(Card card, EncryptionUtil encryptionUtil) {
        String decryptedCardNumber = encryptionUtil.decrypt(card.getCardNumber());
        String maskedCardNumber = "**** **** **** " + decryptedCardNumber.substring(12);

        return new CardResponse(
                maskedCardNumber,
                card.getBalance(),
                card.getExpirationDate(),
                card.getUser().getFullName(),
                card.getStatus().name());
    }
}
