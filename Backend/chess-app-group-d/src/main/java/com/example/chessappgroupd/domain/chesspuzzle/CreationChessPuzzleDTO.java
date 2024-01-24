package com.example.chessappgroupd.domain.chesspuzzle;

// you can either send one fen Notation or several in a single string like this:
// notation1,notation2,notation3
// Fen Noatations will not be checked, make sure they are correct


public class CreationChessPuzzleDTO {

    private String fenNotations;

    public CreationChessPuzzleDTO(String fenNotations) {
        this.fenNotations = fenNotations;
    }

    public CreationChessPuzzleDTO() {

    }

    public String getFenNotations() {
        return fenNotations;
    }

    public void setFenNotations(String fenNotations) {
        this.fenNotations = fenNotations;
    }
}

