package com.example.chessappgroupd.domain.gamehistory;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.game.BoardStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class GameHistory {
    @Id
    private Long gameId;
    @ManyToOne
    private AppUser white;
    @ManyToOne
    private AppUser black;
    private Boolean whitePlayerWon;
    @ElementCollection
    private List<BoardStatus> fen = new ArrayList<BoardStatus>();
    @ElementCollection
    private List<String> moves = new ArrayList<String>();
}
