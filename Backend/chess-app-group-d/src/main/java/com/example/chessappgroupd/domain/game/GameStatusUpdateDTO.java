package com.example.chessappgroupd.domain.game;


public class GameStatusUpdateDTO {
    private String status;
    private String gameId;


    public GameStatusUpdateDTO() {

    }

    public String getStatus() {
        return status;
    }

    public String getGameId() {
        return gameId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}