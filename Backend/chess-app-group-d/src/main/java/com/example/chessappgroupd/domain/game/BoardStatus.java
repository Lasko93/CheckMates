package com.example.chessappgroupd.domain.game;

import jakarta.persistence.Embeddable;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

//Have a look at FEN Notation -> here also as comment in stockfish->StockfishWrapper
@Embeddable
public class BoardStatus {

    private String value;

    protected BoardStatus() {
        //JPA
    }

    public BoardStatus(String value) {
        this.value = notNull(value, "may not be null");
    }

    public String getValue() {
        return value;
    }

public void setValue(String value) {
        this.value = notNull(value, "may not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardStatus that = (BoardStatus) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "BoardStatus{" +
                "value='" + value + '\'' +
                '}';
    }
}
