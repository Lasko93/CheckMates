package com.example.chessappgroupd.repository;

import com.example.chessappgroupd.domain.chesspuzzle.ChessPuzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChessPuzzleRepository extends JpaRepository<ChessPuzzle, Long> {
}
