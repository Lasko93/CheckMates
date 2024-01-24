package com.example.chessappgroupd.domain.chat;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CHATMESSAGE")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MESSAGE_ID")
    private Long id;
    @Column(name = "MESSAGE_CHATID")
    private String chatId;
    @Column(name = "MESSAGE_SENDER")
    private String sender;
    @Column(name = "MESSAGE_MESSAGE")
    private String message;
    @Column(name = "MESSAGE_TIMESTAMP")
    private Date timeStamp;
    @Column(name = "MESSAGE_SEEN")
    private boolean read;
}
