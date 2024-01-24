package com.example.chessappgroupd.domain.game;

import jakarta.persistence.Embeddable;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

//altered Timer to seconds! in int
@Embeddable
public class Timer {

    private Integer WhiteTimerMinutes;
    private Integer WhiteTimerSeconds;
    private Integer BlackTimerMinutes;
    private Integer BlackTimerSeconds;

    public Timer() {
        // This is for Hibernate
    }

    public Timer(Integer whiteTimer, Integer blackTimer) {
        WhiteTimerMinutes = whiteTimer;
        WhiteTimerSeconds = 0;
        BlackTimerMinutes = blackTimer;
        BlackTimerSeconds = 0;
    }

    public Integer getWhiteTimerMinutes() {
        return WhiteTimerMinutes;
    }

    public Integer getWhiteTimerSeconds() {
        return WhiteTimerSeconds;
    }

    public Integer getBlackTimerMinutes() {
        return BlackTimerMinutes;
    }

    public Integer getBlackTimerSeconds() {
        return BlackTimerSeconds;
    }

    public void setWhiteTimerMinutes(Integer whiteTimerMinutes) {
        WhiteTimerMinutes = whiteTimerMinutes;
    }

    public void setWhiteTimerSeconds(Integer whiteTimerSeconds) {
        WhiteTimerSeconds = whiteTimerSeconds;
    }

    public void setBlackTimerMinutes(Integer blackTimerMinutes) {
        BlackTimerMinutes = blackTimerMinutes;
    }

    public void setBlackTimerSeconds(Integer blackTimerSeconds) {
        BlackTimerSeconds = blackTimerSeconds;
    }

    // Add methods to decrease the timer

    public void decreaseWhiteTimer() {
        if (WhiteTimerSeconds > 0) {
            WhiteTimerSeconds--;
        } else if (WhiteTimerMinutes > 0) {
            WhiteTimerMinutes--;
            WhiteTimerSeconds = 59;
        }
    }

    public void decreaseBlackTimer() {
        if (BlackTimerSeconds > 0) {
            BlackTimerSeconds--;
        } else if (BlackTimerMinutes > 0) {
            BlackTimerMinutes--;
            BlackTimerSeconds = 59;
        }
    }

}