/*
DEPRECATED



package com.example.chessappgroupd.domain.gamehistory;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/game/history/lastThreeGames")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LastGamesController {
    private final LastGamesService lastGamesService;
    @GetMapping("/")
    public ResponseEntity<List<LastGamesDTO>> findAll() {
        return ResponseEntity.ok().body(lastGamesService.findAll());
    }
    @GetMapping("/{userName}")
    public ResponseEntity<?> findByLastGamesId(@PathVariable String userName) {
        try {
            return ResponseEntity.ok().body(lastGamesService.findByLastGamesId(userName));
        }catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/player/{userName}")
    public ResponseEntity<?> findGameHistoryByPlayerUserName(@PathVariable String userName) {
        try {
            return ResponseEntity.ok().body(gameHistoryService.findGameHistoryByPlayerUserName(userName));
        }catch (ResponseStatusException | UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //delete
    @DeleteMapping("/{gameId}/delete")
    public ResponseEntity<?> deleteGameHistoryByGameHistoryId(@PathVariable Long gameId) {
        try {
            return ResponseEntity.ok().body(gameHistoryService.deleteGameHistoryByGameHistoryId(gameId));
        }catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //delete all game history by player username
    @DeleteMapping("/{userName}/delete")
    public ResponseEntity<?> deleteLastGamesByPlayerUserName(@PathVariable String userName) {
        try {
            return ResponseEntity.ok().body(lastGamesService.deleteLastGamesByPlayerUserName(userName));
        }catch (ResponseStatusException | UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
*/
