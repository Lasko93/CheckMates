/*
DEPRECATED



package com.example.chessappgroupd.domain.gamehistory;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class LastGamesDTO {
    String userName;
    List<GameHistoryDTO> lastGames;


    public LastGamesDTO(String userName,
                        List<GameHistory> lastGames) {
        this.userName = userName;
        this.lastGames = convertGameHistoryInListToDTO(lastGames);
    }
    private List<GameHistoryDTO> convertGameHistoryInListToDTO(List<GameHistory> x) {
        List<GameHistoryDTO> result = new ArrayList<>();
        for (GameHistory gameHistory : x) {
            result.add(GameHistoryDTO.getDetailsFromGameHistoryDTO(gameHistory));
        }
        return result;
    }
    public static LastGamesDTO getDetailsFromLastGamesDTO(LastGames lastGames) {
        return new LastGamesDTO(
                lastGames.getUserName(),
                lastGames.getLastGames());
    }
}
*/
