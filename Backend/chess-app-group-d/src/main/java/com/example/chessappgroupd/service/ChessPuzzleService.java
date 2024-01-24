package com.example.chessappgroupd.service;

import com.example.chessappgroupd.domain.chesspuzzle.ChessPuzzle;
import com.example.chessappgroupd.repository.ChessPuzzleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChessPuzzleService {

    @Autowired
    private ChessPuzzleRepository chessPuzzleRepository;

    public List<ChessPuzzle> findAll() {
        return chessPuzzleRepository.findAll();
    }

    public Optional<ChessPuzzle> findById(Long id) {
        return chessPuzzleRepository.findById(id);
    }

    public void addChessPuzzle(ChessPuzzle chessPuzzle) {
        chessPuzzleRepository.save(chessPuzzle);
    }

    public void deleteById(Long id) {
        chessPuzzleRepository.deleteById(id);
    }

    public void deleteAll() {
        chessPuzzleRepository.deleteAll();
    }

}
