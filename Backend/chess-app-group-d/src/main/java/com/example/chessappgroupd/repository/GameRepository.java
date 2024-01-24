package com.example.chessappgroupd.repository;

import com.example.chessappgroupd.domain.game.Game;
import com.example.chessappgroupd.domain.game.Timer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    //returning all games where white ID
    @Query("SELECT g FROM Game g WHERE g.white.userName = :username AND NOT g.status = com.example.chessappgroupd.domain.game.Status.FINISHED")
    List<Game> findGameByWhiteUserName(@Param("username") String username);

    //returning all games where black ID
    @Query("SELECT g FROM Game g WHERE g.black.userName = :username AND NOT g.status = com.example.chessappgroupd.domain.game.Status.FINISHED")
    List<Game> findGameByBlackUserName(@Param("username") String username);

    //returning all PENDING Games for whiteID
    @Query("SELECT g FROM Game g WHERE (g.white.userName = :username) AND g.status = com.example.chessappgroupd.domain.game.Status.PENDING")
    List<Game> findPendingGamesByWhiteId(@Param("username") String username);

    // returning all STARTED games whitId
    @Query("SELECT g FROM Game g WHERE (g.white.userName = :username) AND g.status = com.example.chessappgroupd.domain.game.Status.PLAYING")
    List<Game> findPlayingGamesByWhiteId(@Param("username") String username);

    @Query("SELECT g FROM Game g WHERE (g.black.userName = :username) AND g.status = com.example.chessappgroupd.domain.game.Status.PLAYING")
    List<Game> findPlayingGamesByBlackId(@Param("username") String username);

    //TODO: getAllGamesOfBlack und GetAllGamesOfWhite wo game abgebrochen wurde damit es automatisch beendet wird und man nicht weiter spielen kann

    //maybe not required?
    @Query("SELECT g FROM Game g WHERE g.id = :gameId")
    Optional<Game> getOptionalGameById(@Param("gameId") Long gameId);

    @Transactional
    @Modifying
    @Query("UPDATE Game g SET g.timer = :timer WHERE g.id = :gameId")
    void updateGameTimer(@Param("gameId") Long gameId, @Param("timer") Timer timer);

    //YUU FOR GAME HISTORY and APP USER
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM Game g WHERE g.id = :gameId")
    Boolean existsByGame(Long gameId);
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM Game g WHERE g.white.userName = :userName OR g.black.userName = :userName")
    Boolean existsByPlayerUserName(String userName);
    @Modifying
    @Query("DELETE FROM Game g WHERE g.white.userName = :userName OR g.black.userName = :userName")
    void deleteByPlayerUserName(String userName);
    //
}
