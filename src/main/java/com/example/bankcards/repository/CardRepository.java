package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByCardNumber(String cardNumber);
@Query("select c from Card c where c.user.id =: userId")
    List<Card> findByUserId(Long userId);

    default Card findByIdOrElseThrow(Long cardId){
        return findById(cardId).orElseThrow(()->new NotFoundException("Card"+cardId+"не найден"));
    }
}

