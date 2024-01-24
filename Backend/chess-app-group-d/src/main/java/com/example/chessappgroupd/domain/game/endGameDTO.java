package com.example.chessappgroupd.domain.game;

import com.example.chessappgroupd.domain.appUser.AppUser;

public class endGameDTO {

    private String gameId;
    private String winner;
    private boolean isDraw;
    private boolean gaveUp;

    public endGameDTO(String gameId, String winner, boolean isDraw) {
        this.gameId = gameId;
        this.winner = winner;
        this.isDraw = isDraw;
    }

    public endGameDTO() {
        // for JPA
    }

    public boolean isDraw() {
        return isDraw;
    }

    public void setDraw(boolean draw) {
        isDraw = draw;
    }

    public boolean isGaveUp() {
        return gaveUp;
    }

    public void setGaveUp(boolean gaveUp) {
        this.gaveUp = gaveUp;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }



    public String getGameId() {
        return gameId;
    }

    public String getWinner() {
        return winner;
    }

}
