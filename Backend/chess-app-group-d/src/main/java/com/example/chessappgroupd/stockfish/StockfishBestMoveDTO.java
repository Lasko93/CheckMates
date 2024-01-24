package com.example.chessappgroupd.stockfish;

public class StockfishBestMoveDTO {

    private String fenPosition;

    private String answerString;

    public StockfishBestMoveDTO() {

    }

    public StockfishBestMoveDTO(String fenPosition, String answerString) {
        this.fenPosition = fenPosition;
        this.answerString = answerString;
    }

    public String getAnswerString() {
        return answerString;
    }

    public String getFenPosition() {
        return fenPosition;
    }

    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }

    public void setFenPosition(String fenPosition) {
        this.fenPosition = fenPosition;
    }
}

