package com.example.chessappgroupd.domain.game;

import jakarta.persistence.Embeddable;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

@Embeddable
public class ScoreResult {
    private int valueWhite;

    private int valueBlack;

    protected ScoreResult() {
        //JPA
    }

    public ScoreResult(int valueWhite, int valueBlack) {
        this.valueWhite = notNull(valueWhite, "may not be null");
        this.valueBlack = notNull(valueBlack, "may not be null");
    }

    public int getValueWhite() {
        return valueWhite;
    }

    public void setValueWhite(int valueWhite) {
        this.valueWhite = valueWhite;
    }

    public int getValueBlack() {
        return valueBlack;
    }

    public void setValueBlack(int valueBlack) {
        this.valueBlack = valueBlack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreResult that = (ScoreResult) o;
        return valueWhite == that.valueWhite && valueBlack == that.valueBlack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueWhite, valueBlack);
    }

    @Override
    public String toString() {
        return "ScoreResult{" +
                "valueWhite=" + valueWhite +
                ", valueBlack=" + valueBlack +
                '}';
    }
}
