package com.example.chessappgroupd.domain.game;

import com.example.chessappgroupd.domain.appUser.AppUser;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

//maybe solve black issue by inserting dummy per default?
@Entity
@Table(name = "TAB_GAME")

public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GAME_ID")
    private Long id;

    //User creating the game
    @OneToOne
    @JoinColumn(name = "GAME_APPUSER_WHITE_ID", unique = false)
    private AppUser white;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "GAME_APPUSER_BLACK_ID", unique = false)
    private AppUser black;

//    @OneToMany(mappedBy = "game")
// //   @JoinColumn(name = "GAME_GAMEREQUEST_ID")
//    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
//    private List<GameRequest> gameRequests;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GameRequest> gameRequests = new HashSet<>();

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "GAME_GAME_NAME"))
    private GameName gameName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "GAME_BOARD_STATUS"))
    private BoardStatus boardStatus;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "GAME_SCORE_RESULT"))
    private ScoreResult scoreResult;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "GAME_STATUS")
    private Status status;


    @AttributeOverride(name = "value", column = @Column(name = "GAME_TIMER"))
    private Timer timer;

    @AttributeOverride(name = "value", column = @Column(name = "White_Player_Turn"))
    private boolean whitePlayerTurn=true;

    //Not used yet
    @AttributeOverride(name = "value", column = @Column(name = "Game_Paused"))
    private boolean isGamePaused=false;
    //Not used yet
    @AttributeOverride(name = "value", column = @Column(name = "White_Player_Pause_Count"))
    private int whitePlayerPauseCount=0;
    //Not used yet
    @AttributeOverride(name = "value", column = @Column(name = "black_Player_Pause_Count"))
    private int blackPlayerPauseCount=0;


    protected Game() {
        //JPA
    }

    //Constructor:

    //sets fen notation to default start position
    //alter score result later
    //if black should be dummy dont forget to save Object before setting! otherwise transient
    public Game(AppUser white, GameName gameName, Status status, Timer timer, ScoreResult scoreResult) {
        this.white = white;
        this.gameName = gameName;
        this.boardStatus = new BoardStatus("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        this.status = status;
        this.timer = timer;
        this.black = null;
        this.scoreResult = new ScoreResult(10,10);
    }


    //GETTER:

    public Long getId() {
        return id;
    }

    public AppUser getWhite() {
        return white;
    }

    public AppUser getBlack() {
        return black;
    }

    public GameName getGameName() {
        return gameName;
    }

    public BoardStatus getBoardStatus() {
        return boardStatus;
    }

    public void setBoardStatus(BoardStatus boardStatus) {
        this.boardStatus = boardStatus;
    }

    public ScoreResult getScoreResult() {
        return scoreResult;
    }

    public Status getStatus() {
        return status;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setBlack(AppUser black) {
        this.black = black;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isWhitePlayerTurn() {
        return whitePlayerTurn;
    }

    public void setWhitePlayerTurn(boolean whitePlayerTurn) {
        this.whitePlayerTurn = whitePlayerTurn;
    }

    public boolean isGamePaused() {
        return isGamePaused;
    }

    public void setGamePaused(boolean gamePaused) {
        isGamePaused = gamePaused;
    }

    public int getWhitePlayerPauseCount() {
        return whitePlayerPauseCount;
    }

    public void setWhitePlayerPauseCount(int whitePlayerPauseCount) {
        this.whitePlayerPauseCount = whitePlayerPauseCount;
    }

    public int getBlackPlayerPauseCount() {
        return blackPlayerPauseCount;
    }

    public void setBlackPlayerPauseCount(int blackPlayerPauseCount) {
        this.blackPlayerPauseCount = blackPlayerPauseCount;
    }

    //hash and equal

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(white, game.white) && Objects.equals(black, game.black) && Objects.equals(gameName, game.gameName) && Objects.equals(boardStatus, game.boardStatus) && Objects.equals(scoreResult, game.scoreResult) && status == game.status && Objects.equals(timer, game.timer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, white, black, gameName, boardStatus, scoreResult, status, timer);
    }


    //to string


    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", white=" + white +
                ", black=" + black +
                ", gameRequests=" +
                ", gameName=" + gameName +
                ", boardStatus=" + boardStatus +
                ", scoreResult=" + scoreResult +
                ", status=" + status +
                ", timer=" + timer +
                '}';
    }
}
