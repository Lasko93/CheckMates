package com.example.chessappgroupd.domain.game;

import com.example.chessappgroupd.domain.appUser.AppUser;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Optional;


@Entity
@Table(name = "TAB_GAMEREQUEST",
        uniqueConstraints = @UniqueConstraint(columnNames = {"GAMEREQUEST_WHITE_USER_ID", "GAMEREQUEST_BLACK_USER_ID"}))
public class GameRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GAMEREQUEST_ID" , unique = false)
    private Long id;

    //User sending request
    @ManyToOne
    @JoinColumn(name = "GAMEREQUEST_WHITE_USER_ID" ,unique = false)
    private AppUser white;

    //User receiving request
    @ManyToOne
    @JoinColumn(name = "GAMEREQUEST_BLACK_USER_ID")
    private AppUser black;

    @ManyToOne
    @JoinColumn(name = "GAMEREQUEST_GAME_ID")
    private Game game;

    @Enumerated(EnumType.STRING)
    @Column(name = "GAMEREQUEST_REQUESTSTATUS")
    private RequestStatus requestStatus;

    //constructors:
    protected GameRequest() {
        // JPA
    }

    public GameRequest(AppUser white, AppUser black, Game game, RequestStatus requestStatus) {
        this.white = white;
        this.black = black;
        this.game = game;
        this.requestStatus = requestStatus;
    }

    // Add getters and setters

    public Long getId() {
        return id;
    }

    public AppUser getWhite() {
        return white;
    }

    public AppUser getBlack() {
        return black;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public Game getGame() {
        return game;
    }

    //setter only for AppUser Black and Request Status because is invited and AppUserWhite is always person creating game

    public void setBlack(AppUser black) {
        this.black = black;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameRequest that = (GameRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(white, that.white) && Objects.equals(black, that.black) && Objects.equals(game, that.game) && requestStatus == that.requestStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, white, black, game, requestStatus);
    }

    @Override
    public String toString() {
        return "GameRequest{" +
                "id=" + id +
                ", white=" + white +
                ", black=" + black +
                ", game=" + game +
                ", requestStatus=" + requestStatus +
                '}';
    }
}
