package com.example.chessappgroupd.service;

import com.example.chessappgroupd.domain.chesspuzzle.ChessPuzzle;
import com.example.chessappgroupd.domain.chesspuzzle.ExpertTrophy;
import com.example.chessappgroupd.repository.ChessPuzzleRepository;
import com.example.chessappgroupd.repository.ExpertTrophyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpertTrophyService {

    @Autowired
    private ExpertTrophyRepository expertTrophyRepository;

    public List<ExpertTrophy> findAll() {
        return expertTrophyRepository.findAll();
    }

    public Optional<ExpertTrophy> findById(String id) {
        return expertTrophyRepository.findById(id);
    }

    public void addExpertTrophy(ExpertTrophy expertTrophy) {
        expertTrophyRepository.save(expertTrophy);
    }

    public void deleteById(String id) {
        expertTrophyRepository.deleteById(id);
    }

    public void deleteAll() {
        expertTrophyRepository.deleteAll();
    }
}
