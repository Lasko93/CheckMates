package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.game.Game;
import com.example.chessappgroupd.domain.game.GameRequest;
import com.example.chessappgroupd.domain.game.RequestStatus;
import com.example.chessappgroupd.domain.game.Status;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.service.GameRequestService;
import com.example.chessappgroupd.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/game-request")
@CrossOrigin(origins = "http://localhost:3000")
public class GameRequestController {

    //Info on GameRequestController:
    //GameRequest holds information on all sent GameRequests that can either be found via username or GameRequestID
    //when sending a GameRequest, an instance will be created and saved with info: AppUser white (creator), AppUser black (receiver), GameID, RequestStatus will per default be set to PENDING
    //There will be access point in order to accept game, by checking if game is already ACCEPTED if still PENDING, Game will have an access point that will be called by GameRequest to change that
    //and give full info on who AppUser Black is, Game Status will be set to ACCEPTED then


    @Autowired
    private GameRequestService gameRequestService;

    @Autowired
    private GameService gameService;

    @Autowired
    private AppUserRepository appUserRepository;

    //maybe delete this method?
    @GetMapping("/find-all")
    public ResponseEntity<List<GameRequest>> allGameRequests() {
        List<GameRequest> gameRequests = gameRequestService.findAll();
        return new ResponseEntity<>(gameRequests, HttpStatus.OK);
    }

    @GetMapping("/find-game-request-white-id/{username}")
    public ResponseEntity<List<GameRequest>> findGameRequestsByWhiteUserName(@PathVariable("username") String username) {
        List<GameRequest> gameRequests = gameRequestService.findGameRequestsByWhiteUserName(username);
        return new ResponseEntity<>(gameRequests, HttpStatus.OK);
    }

    @GetMapping("/find-game-request-black-id/{username}")
    public ResponseEntity<List<GameRequest>> findGameRequestsByBlackUserName(@PathVariable("username") String username) {
        List<GameRequest> gameRequests = gameRequestService.findGameRequestsByBlackUserName(username);
        return new ResponseEntity<>(gameRequests, HttpStatus.OK);
    }

    @GetMapping("/find-game-request-black-pending/{username}")
    public ResponseEntity<List<GameRequest>> findPendingGameRequestsByBlackId(@PathVariable("username") String username) {
        List<GameRequest> gameRequests = gameRequestService.findPendingGameRequestsByBlackId(username);
        return new ResponseEntity<>(gameRequests, HttpStatus.OK);
    }

    @GetMapping("/find-game-request-white-pending/{username}")
    public ResponseEntity<List<GameRequest>> findPendingGameRequestsByWhiteId(@PathVariable("username") String username) {
        List<GameRequest> gameRequests = gameRequestService.findPendingGameRequestsByWhiteId(username);
        return new ResponseEntity<>(gameRequests, HttpStatus.OK);
    }


    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        GameRequest gameRequest = gameRequestService.findById(id).orElse(null);
        if (gameRequest != null) {
            return new ResponseEntity<>(gameRequest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("gameRequest not found", HttpStatus.NOT_FOUND);
        }
    }


    //when sending a GameRequest, an instance will be created and saved with info: AppUser white (creator), AppUser black (receiver), GameID, RequestStatus will per default be set to PENDING
    //maybe add check if AppUsers and so on can all be found and so on
    @PostMapping("/create-game-request")
    public ResponseEntity<?> createGameRequest(@RequestParam String whiteId, @RequestParam String blackId, @RequestParam String gameId) {
        AppUser white = appUserRepository.findById(whiteId).get();
        AppUser black = appUserRepository.findById(blackId).get();
        //catch exception here?
        Long gameLong = Long.parseLong(gameId);
        Game game = gameService.findById(gameLong).get();

        if (!((white == null) && (black == null) && (game == null))) {
            GameRequest newGameRequest = new GameRequest(white, black, game, RequestStatus.PENDING);
            gameRequestService.addGameRequest(newGameRequest);

            return new ResponseEntity<>(newGameRequest, HttpStatus.CREATED);
        }

        return new ResponseEntity<>("black, white or game null", HttpStatus.NOT_FOUND);
    }

    //There will be access point in order to accept game, by checking if game is already ACCEPTED if still PENDING, Game will have an access point that will be called by GameRequest to change that
    //and give full info on who AppUser Black is, Game Status will be set to ACCEPTED then
    //This method can only be accepted by users that have been requested and will be AppUser black in game
    @PostMapping("/accept-game-request/{requestId}")
    public ResponseEntity<?> acceptGameRequest(@PathVariable("requestId") Long requestId,
                                               @RequestParam String blackUsername) {
        Optional<GameRequest> optionalGameRequest = gameRequestService.findById(requestId);
        if (!optionalGameRequest.isPresent()) {
            return new ResponseEntity<>("GameRequest not found", HttpStatus.NOT_FOUND);
        }
        GameRequest gameRequest = optionalGameRequest.get();

        Optional<AppUser> optionalAppUser = appUserRepository.findById(blackUsername);
        if (!optionalAppUser.isPresent()) {
            return new ResponseEntity<>("AppUser not found", HttpStatus.NOT_FOUND);
        }
        AppUser appUser = optionalAppUser.get();

        Game game = gameRequest.getGame();
        if (game == null) {
            return new ResponseEntity<>("Game not found", HttpStatus.NOT_FOUND);
        }

        // Check if game is still pending and can be accepted
        if (!game.getStatus().equals(Status.PENDING) || game.getBlack() != null) {
            // Game is not Pending or already has a black player -> non-valid request
            return new ResponseEntity<>("Game is not in a valid state to be accepted or lobby is full", HttpStatus.BAD_REQUEST);
        }

        // Update game status and set user as black player
        game.setStatus(Status.ACCEPTED);
        game.setBlack(appUser);

        // Update request status
        gameRequest.setRequestStatus(RequestStatus.ACCEPTED);

        // Save changes to the database
        gameService.addGame(game);
        gameRequestService.addGameRequest(gameRequest); // Save the updated request
        // Delete the game request after setting it to ACCEPTED
        gameRequestService.deleteGameRequest(requestId);

        // Return successful change and acceptance of request
        return new ResponseEntity<>(gameRequest, HttpStatus.ACCEPTED);
    }


    //If User declines GameRequest
    @PostMapping("/decline-game-request/{requestId}")
    public ResponseEntity<?> declineGameRequest(@PathVariable("requestId") Long requestId,
                                                @RequestParam String blackUsername) {
        GameRequest gameRequest = gameRequestService.findById(requestId).get();

        if (gameRequest != null) {
            gameRequest.setRequestStatus(RequestStatus.DECLINED);
               gameRequestService.deleteGameRequest(requestId);
            return new ResponseEntity<>(gameRequest, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("gameRequest not found", HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<?> deleteGameRequest(@PathVariable("id") Long id) {
        GameRequest gameRequest = gameRequestService.findById(id)
                .orElse(null);

        if (gameRequest == null) {
            return new ResponseEntity<>("Game Request not found", HttpStatus.NOT_FOUND);
        }

        // Delete the game request from the repository
        gameRequestService.deleteGameRequest(id);

        return new ResponseEntity<>("Game Request deleted successfully", HttpStatus.OK);
    }
}
