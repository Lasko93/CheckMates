package com.example.chessappgroupd.domain.gamehistory;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/game/history")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class GameHistoryController {
    private final GameHistoryService gameHistoryService;
    @GetMapping("/")
    public ResponseEntity<List<GameHistoryDTO>> findAll() {
        return ResponseEntity.ok().body(gameHistoryService.findAll());
    }
    @GetMapping("/{gameId}")
    public ResponseEntity<?> findByGameHistoryId(@PathVariable Long gameId) {
        try {
            return ResponseEntity.ok().body(gameHistoryService.findByGameHistoryId(gameId));
        }catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/player/{userName}")
    public ResponseEntity<?> findGameHistoryByPlayerUserName(@PathVariable String userName) {
        try {
            return ResponseEntity.ok().body(gameHistoryService.findAllGameHistoryByPlayerUserName(userName));
        }catch (ResponseStatusException | UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //WICHTIG FRONTEND
    @GetMapping("/lastThree/player/{userName}")
    public ResponseEntity<?> findLastThreeGameHistoriesByPlayerUserName(@PathVariable String userName) {
        try {
            return ResponseEntity.ok().body(gameHistoryService.findLastThreeGameHistoriesByPlayerUserName(userName));
        }catch (ResponseStatusException | UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //delete game history by gameId
    @DeleteMapping("/{gameId}/delete")
    public ResponseEntity<?> deleteGameHistoryByGameHistoryId(@PathVariable Long gameId) {
        try {
            return ResponseEntity.ok().body(gameHistoryService.deleteOneGameHistoryByGameHistoryId(gameId));
        }catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //delete game history by player username
    @DeleteMapping("/player/{userName}/delete")
    public ResponseEntity<?> deleteAllGameHistoryByPlayerUserName(@PathVariable String userName) {
        try {
            return ResponseEntity.ok().body(gameHistoryService.deleteOnlyGameHistoryByPlayerUserName(userName));
        }catch (ResponseStatusException | UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
