package com.example.chessappgroupd.domain.gamehistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
//Wenn die Transaktion erfolgreich ist, werden die Änderungen an der Datenbank gespeichert. Wenn die Transaktion fehlschlägt, werden die Änderungen rückgängig gemacht.
public interface GameHistoryRepository extends JpaRepository<GameHistory, Long> {
    //GameHistory findByGameId(Long gameId);
    @Query("SELECT gh FROM GameHistory gh WHERE gh.gameId = :gameId")
    GameHistory findAllGameHistoryByGameHistoryId(Long gameId);
    @Query("SELECT gh FROM GameHistory gh WHERE gh.black.userName = :userName OR gh.white.userName = :userName")
    List<GameHistory> findAllGameHistoryByPlayerUserName(String userName);
    //WICHTIG FRONTEND
    @Query("SELECT gh FROM GameHistory gh WHERE gh.white.userName = :userName OR gh.black.userName = :userName ORDER BY gh.gameId DESC LIMIT 3")
    List<GameHistory> findLastThreeGameHistoriesByPlayerUserName(String userName);
    @Query("SELECT CASE WHEN COUNT(gh) > 0 THEN true ELSE false END FROM GameHistory gh WHERE gh.gameId = :gameId")
    Boolean existsByGameHistory(Long gameId);
    @Query("SELECT CASE WHEN COUNT(gh) > 0 THEN true ELSE false END FROM GameHistory gh WHERE gh.white.userName = :userName OR gh.black.userName = :userName")
    Boolean existsByPlayerUserName(String userName);
    @Modifying
    @Query("DELETE FROM GameHistory gh WHERE gh.white.userName = :userName AND gh.black.userName = :userName")
    void deleteGameHistoryWithTwoPlaceholder(String userName);
}
