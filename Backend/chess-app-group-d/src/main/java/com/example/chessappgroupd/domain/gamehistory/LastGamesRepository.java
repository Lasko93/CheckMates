/*
DEPRECATED



package com.example.chessappgroupd.domain.gamehistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface LastGamesRepository extends JpaRepository<LastGames, String> {
    @Query("SELECT lg FROM LastGames lg WHERE lg.userName = :userName")
    LastGames findByLastGamesId(String userName);
    @Query("SELECT lg.lastGames FROM LastGames lg WHERE lg.userName = :userName")
    List<LastGames> findLastGamesListByUserName(String userName);
    @Query("SELECT CASE WHEN COUNT(lg) > 0 THEN true ELSE false END FROM LastGames lg WHERE lg.userName = :userName")
    Boolean existsByPlayerUserName(String userName);
    @Modifying
    @Query("DELETE FROM LastGames lg WHERE lg.userName = :userName")
    void deleteLastGamesByPlayerUserName(String userName);
}
*/
