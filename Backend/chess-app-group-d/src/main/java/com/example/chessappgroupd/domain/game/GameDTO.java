package com.example.chessappgroupd.domain.game;

public class GameDTO {
    private String username;
    private String gameName;
    private Integer timer;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Integer getTimer() {
        return timer;
    }

    public void setTimer(Integer timerInt) {
        this.timer = timerInt;
    }
}

