package com.example.chessappgroupd.domain.chesspuzzle;

import com.example.chessappgroupd.domain.game.BoardStatus;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "TAB_CHESSPUZZLE")
public class ChessPuzzle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHESSPUZZLE_ID")
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "CHESSPUZZLE_BOARDSTATUS"))
    private BoardStatus boardStatus;

//    @ManyToOne
//    private ExpertTrophy expertTrophy;


    //constructor
    protected ChessPuzzle() {
        //JPA
    }

    public ChessPuzzle(BoardStatus boardStatus) {
        this.boardStatus = boardStatus;
    }


    //getter
    public Long getId() {
        return id;
    }

    public BoardStatus getBoardStatus() {
        return boardStatus;
    }


    //necessary methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPuzzle that = (ChessPuzzle) o;
        return Objects.equals(id, that.id) && Objects.equals(boardStatus, that.boardStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, boardStatus);
    }

    @Override
    public String toString() {
        return "ChessPuzzle{" +
                "id=" + id +
                ", boardStatus=" + boardStatus +
                '}';
    }
}
