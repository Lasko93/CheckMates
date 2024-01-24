package com.example.chessappgroupd.repository;

import com.example.chessappgroupd.domain.game.GameRequest;
import com.example.chessappgroupd.domain.game.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//changed white and black bc were switched accidentally
//adding findbyIdi handwritten
//maybe bc optional black is always null in findbyId

@Repository
public interface GameRequestRepository extends JpaRepository<GameRequest, Long> {
    @Query("SELECT gr FROM GameRequest gr WHERE gr.white.userName = :username")
    List<GameRequest> findGameRequestsByWhiteUserName(@Param("username") String username);

    //returns all GameRequests containing username white
    @Query("SELECT gr FROM GameRequest gr WHERE gr.black.userName = :username")
    List<GameRequest> findGameRequestsByBlackUserName(@Param("username") String username);

    //returning all Pending Requests by WhiteId
    @Query("SELECT gr FROM GameRequest gr WHERE (gr.black.userName = :username) AND gr.requestStatus = 'PENDING'")
    List<GameRequest> findPendingGameRequestsByBlackId(@Param("username") String username);

    //returning all Pending Requests by BlackId
    @Query("SELECT gr FROM GameRequest gr WHERE (gr.white.userName = :username) AND gr.requestStatus = 'PENDING'")
    List<GameRequest> findPendingGameRequestsByWhiteId(@Param("username") String username);
}
