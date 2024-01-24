package com.example.chessappgroupd.domain.game;


public class PauseOrResumeDTO {

    private String gameId;
    private String playerColor;
    private boolean isPause;

    public PauseOrResumeDTO() {
        // Default constructor
    }


    public PauseOrResumeDTO(String gameId, String playerColor, boolean isPause) {
        this.gameId = gameId;
        this.playerColor = playerColor;
        this.isPause = isPause;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }


}
