package com.example.chessappgroupd.domain.game;

public class BoardStatusUpdateDTO {
    private String fen;
    private String gameId;
    private Boolean whitePlayerTurn;
    private Boolean check;
    private Boolean checkMate;
    private Boolean remis;
    private String sanMove;

    public BoardStatusUpdateDTO() {

    }

    public BoardStatusUpdateDTO(String gameId, Boolean whitePlayerTurn, String fen, Boolean checkMate, Boolean remis, String sanMove) {
        this.gameId = gameId;
        this.whitePlayerTurn = whitePlayerTurn;
        this.fen = fen;
        this.checkMate = checkMate;
        this.remis = remis;
        this.sanMove = sanMove;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Boolean getCheckMate() {
        return checkMate;
    }

    public void setCheckMate(Boolean checkMate) {
        this.checkMate = checkMate;
    }

    public Boolean getRemis() {
        return remis;
    }

    public void setRemis(Boolean remis) {
        this.remis = remis;
    }

    public String getFen() {
        return fen;
    }

    public String getGameId() {
        return gameId;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Boolean getWhitePlayerTurn() {
        return whitePlayerTurn;
    }

    public void setWhitePlayerTurn(Boolean whitePlayerTurn) {
        this.whitePlayerTurn = whitePlayerTurn;
    }

    public String getSanMove() {
        return sanMove;
    }

    public void setSanMove(String sanMove) {
        this.sanMove = sanMove;
    }
}
