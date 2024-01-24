package com.example.chessappgroupd.domain.game;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class GameName {

    private String value;

    protected GameName() {
        //JPA
    }

    public GameName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameName gameName = (GameName) o;
        return Objects.equals(value, gameName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "GameName{" +
                "value='" + value + '\'' +
                '}';
    }
}
