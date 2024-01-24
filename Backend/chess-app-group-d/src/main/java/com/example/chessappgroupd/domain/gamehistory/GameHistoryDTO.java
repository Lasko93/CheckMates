package com.example.chessappgroupd.domain.gamehistory;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.appUser.AppUserDTO;
import com.example.chessappgroupd.domain.game.BoardStatus;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class GameHistoryDTO {
    Long gameId;
    AppUserDTO white;
    AppUserDTO black;
    Boolean whitePlayerWon;
    List<BoardStatus> fen;
    public GameHistoryDTO (Long gameId,
                        AppUser white,
                        AppUser black,
                        Boolean whitePlayerWon,
                        List<BoardStatus> fen){
        this.gameId = gameId;
        this.white = AppUserDTO.getSafeDetailsFromUserDTO(white);
        this.black = AppUserDTO.getSafeDetailsFromUserDTO(black);
        this.whitePlayerWon = whitePlayerWon;
        this.fen = fen;
    }
    public static GameHistoryDTO getDetailsFromGameHistoryDTO(GameHistory gameHistory) {
        return new GameHistoryDTO(
                gameHistory.getGameId(),
                gameHistory.getWhite(),
                gameHistory.getBlack(),
                gameHistory.getWhitePlayerWon(),
                gameHistory.getFen());
    }
}
