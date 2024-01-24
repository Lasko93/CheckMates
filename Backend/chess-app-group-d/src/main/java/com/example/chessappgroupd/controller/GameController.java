package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.game.*;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.repository.GameRepository;
import com.example.chessappgroupd.service.GameService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

//altered param not body

//altered find by id not username
@RestController
@RequestMapping(path = "/game")
@CrossOrigin(origins = "http://localhost:3000")
public class GameController {
    //game logic methods: There are methods for displaying game info such as finding by AppUser black or white
    //When creating a game Frontend sends a gameName, username, Time then game will be created with limited information
    //-> Game will get status Pending until GameRequests have been sent and accepted
    //-> when sending GameRequests, you can find PendingGames for AppUser, send GameId and userName white and black to GameRequest
    //-> GameRequest will store all Information and will have API endpoint to switch Game status from PENDING to ACCEPTED when black AppUser accepts game and not started yet
    //-> Game will have method to stop game while playing, whenever switched to PLAYING or Stopped Save TIME for frontend to access
    @Autowired
    private GameService gameService;


    @Autowired
    private GameRepository gameRepository;


    @Autowired
    private AppUserRepository appUserRepository;


    //ALL GET REQUESTS

    //might be deleted
    @GetMapping("/find-all")
    public ResponseEntity<List<Game>> allGames() {
        return new ResponseEntity<>(gameService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/find-game-white-username/{username}")
    public ResponseEntity<List<Game>> findGameByWhiteUserName(@PathVariable("username") String username) {
        List<Game> games = gameService.findGameByWhiteUserName(username);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/find-game-black-username/{username}")
    public ResponseEntity<List<Game>> findGameByBlackUserName(@PathVariable("username") String username) {
        List<Game> games = gameService.findGameByBlackUserName(username);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }


    @GetMapping("/find-game-white-username-pending/{username}")
    public ResponseEntity<List<Game>> findPendingGamesByWhiteId(@PathVariable("username") String username) {
        List<Game> games = gameService.findPendingGamesByWhiteId(username);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/find-game-white-username-playing/{username}")
    public ResponseEntity<List<Game>> findPlayingGamesByWhiteId(@PathVariable("username") String username) {
        List<Game> games = gameService.findPlayingGamesByWhiteId(username);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }


    @GetMapping("/find-game-black-username-playing/{username}")
    public ResponseEntity<List<Game>> findPlayingGamesByBlackId(@PathVariable("username") String username) {
        List<Game> games = gameService.findPlayingGamesByBlackId(username);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    //finding game by game id!
    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<?> findByID(@PathVariable("id") Long id) {
        try {
            Game game = gameService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Game with ID " + id + " not found"));
            return new ResponseEntity<>(game, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // Return a user-friendly response without logging
            return new ResponseEntity<>("Game with ID " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }


    //ALL POST REQUESTS


    //add Game
    //maybe delete this function?
    // @PostMapping("/post")
    // public void addUser(Game game) {
    //     this.gameService.addGame(game);
    // }

    @PostMapping("/create-game")
    public ResponseEntity<?> createGame(@RequestBody GameDTO request) {
        AppUser white = appUserRepository.findById(request.getUsername()).orElse(null);
        GameName gameName = new GameName(request.getGameName());
        if (white != null) {
            Timer timer = new Timer(request.getTimer(), request.getTimer());
            Game game = new Game(white, gameName, Status.PENDING, timer, new ScoreResult(10, 10));
            gameService.addGame(game);
            return new ResponseEntity<>(game, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    //following methods do alter status for starting and playing game

    // alter this method to suit Timer and its usage in order to start game
    //Maybe check for correct user part of game to be able to stop game

    //start game
    @PostMapping("/start-game/{gameId}")
    public ResponseEntity<?> startGame(@PathVariable("gameId") Long id) {

        Game game = gameService.findById(id).get();

        if (game != null) {
            game.setStatus(Status.PLAYING);
            //call method to set starting time in Timer?

            return new ResponseEntity<>(game, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("game not found", HttpStatus.NOT_FOUND);
    }

    // pause game
    @PostMapping("/pause-game/{gameId}")
    public ResponseEntity<?> pauseGame(@PathVariable("gameId") Long id) {

        Game game = gameService.findById(id).get();
        if (game != null) {
            game.setStatus(Status.STOPPED);
            //call method to stop time in Timer?

            return new ResponseEntity<>(game, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("game not found", HttpStatus.NOT_FOUND);
    }

    //finish game request
    @PostMapping("/finish-game/{gameId}")
    public ResponseEntity<?> finishGame(@PathVariable("gameId") Long id) {


        Game game = gameService.findById(id).get();

        if (game != null) {
            game.setStatus(Status.FINISHED);
            //call method to stop time in Timer?

            return new ResponseEntity<>("game deleted ", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("game not found", HttpStatus.NOT_FOUND);
    }

    //ALL DELETE REQUESTS

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        gameService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update-game-remove-black/{gameId}")
    public ResponseEntity<?> removeBlackPlayer(@PathVariable Long gameId) {
        // Fetch the game by gameId
        Game game = gameService.findById(gameId).orElse(null);

        if (game == null) {
            return new ResponseEntity<>("Game not found", HttpStatus.NOT_FOUND);
        }

        // Remove black player
        game.setBlack(null);

        // Update the game status
        game.setStatus(Status.PENDING);

        gameService.addGame(game); // Save the game

        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PutMapping("/update-time/{gameId}")
    public ResponseEntity<?> updateTime(@PathVariable("gameId") String gameId, @RequestParam("time") String time) {
        try {
            Game game = gameService.findById(Long.parseLong(gameId))
                    .orElseThrow(() -> new EntityNotFoundException("Game with ID " + gameId + " not found"));

            // Update the timer here
            game.getTimer().setWhiteTimerMinutes(Integer.parseInt(time));
            game.getTimer().setWhiteTimerSeconds(0);
            game.getTimer().setBlackTimerMinutes(Integer.parseInt(time));
            game.getTimer().setBlackTimerSeconds(0);

            gameRepository.save(game);

            return new ResponseEntity<>(game, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Game with ID " + gameId + " not found", HttpStatus.NOT_FOUND);
        }
    }

}

