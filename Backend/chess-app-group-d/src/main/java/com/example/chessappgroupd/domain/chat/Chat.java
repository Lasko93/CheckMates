package com.example.chessappgroupd.domain.chat;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CHAT")
public class Chat {

    @Id
    @Column(name = "CHAT_CHATID")
    private String chatId;
    @Column(name = "ISCLUBCHAT")
    private boolean isClubchat;
    @ElementCollection
    @CollectionTable(name = "CHAT_SUBS", joinColumns = @JoinColumn(name = "CHAT_ID"))
    @Column(name = "SUBS")
    private List<String> subs;

}
