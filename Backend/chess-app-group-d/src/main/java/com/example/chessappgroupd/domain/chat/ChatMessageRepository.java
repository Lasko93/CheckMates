package com.example.chessappgroupd.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatId = :id ORDER BY cm.timeStamp")
    List<ChatMessage> findByChatId(@Param("id") String id);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.id = :id")
    ChatMessage findByMessageId(@Param("id") Long id);
}
