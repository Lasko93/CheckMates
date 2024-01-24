package com.example.chessappgroupd.domain.chesspuzzle;

public class ProcessChessPuzzleDTO {

    private String appUserId;

    private String chessPuzzleId;


    public ProcessChessPuzzleDTO() {

    }

    public ProcessChessPuzzleDTO(String appUserId, String chessPuzzleId) {
        this.appUserId = appUserId;
        this.chessPuzzleId = chessPuzzleId;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public String getChessPuzzleId() {
        return chessPuzzleId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public void setChessPuzzleId(String chessPuzzleId) {
        this.chessPuzzleId = chessPuzzleId;
    }
}

