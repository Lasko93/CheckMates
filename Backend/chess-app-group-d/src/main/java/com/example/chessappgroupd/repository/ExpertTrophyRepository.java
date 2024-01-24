package com.example.chessappgroupd.repository;

import com.example.chessappgroupd.domain.chesspuzzle.ExpertTrophy;
import com.example.chessappgroupd.domain.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpertTrophyRepository extends JpaRepository<ExpertTrophy, String> {

//    @Query("SELECT e FROM ExpertTrophy e WHERE e.expertStatus = 'EXPERT'")
//    List<ExpertTrophy> findAllExperts();
}
