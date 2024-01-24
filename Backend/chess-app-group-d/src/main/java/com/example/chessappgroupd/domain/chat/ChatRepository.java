package com.example.chessappgroupd.domain.chat;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional
public interface ChatRepository extends JpaRepository<Chat, String> {
    @Query("SELECT c FROM Chat c WHERE c.chatId = :chatId")
    Chat findByChatId(@Param("chatId") String chatId);

    @Query("SELECT c.chatId FROM Chat c WHERE :user MEMBER OF c.subs")
    List<String> findIfInSubs(@Param("user") String user);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Chat c WHERE c.chatId = :chatId")
    Boolean existsByChatId(String chatId);

    }
