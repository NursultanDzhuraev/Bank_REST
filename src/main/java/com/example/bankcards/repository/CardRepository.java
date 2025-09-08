package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CardRepository extends JpaRepository<Card, Long> {

@Query("select c from Card c where c.user.id =:userId")
    List<Card> findByUserId(@Param("userId")Long userId);

    default Card findByIdOrElseThrow(Long cardId){
        return findById(cardId).orElseThrow(()->new NotFoundException("Card"+cardId+"не найден"));
    }

    Optional<Card> findByCardHash(String cardHash);
}

