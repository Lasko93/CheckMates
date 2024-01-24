package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.chesspuzzle.*;
import com.example.chessappgroupd.domain.game.BoardStatus;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.service.ChessPuzzleService;
import com.example.chessappgroupd.service.ExpertTrophyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.Set;

//altered method bc didnt add puzzle previously!!
//altered string and name of chesspuzzleid
//get into postmapping
@RestController
@RequestMapping(path = "/chesspuzzle")
@CrossOrigin(origins = "http://localhost:3000")
public class ChessPuzzleController {

    @Autowired
    private ChessPuzzleService chessPuzzleService;

    @Autowired
    private ExpertTrophyService expertTrophyService;

    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/find-all")
    public ResponseEntity<List<ChessPuzzle>> allChessPuzzles() {
        return new ResponseEntity<>(chessPuzzleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<?> findByID(@PathVariable("id") Long id) {
        try {
            ChessPuzzle chessPuzzle = chessPuzzleService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("chesspuzzle with ID " + id + " not found"));
            return new ResponseEntity<>(chessPuzzle, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Chesspuzzle with ID " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        chessPuzzleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteAll() {
        chessPuzzleService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //before @RequestParam String fenNotations
    @PostMapping("/create-chesspuzzle")
    public ResponseEntity<?> createChessPuzzle(@RequestBody CreationChessPuzzleDTO creationChessPuzzleDTO) {
        String fenNotations = creationChessPuzzleDTO.getFenNotations();
        try {
            String[] fenArray = fenNotations.split(",");

            for (String fen : fenArray) {
                BoardStatus boardStatus = new BoardStatus(fen);
                ChessPuzzle newChessPuzzle = new ChessPuzzle(boardStatus);
                chessPuzzleService.addChessPuzzle(newChessPuzzle);

            }

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle any exceptions
            return new ResponseEntity<>("Error processing FEN notations", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //use this method to retrieve chessPuzzle
    @GetMapping("/get-random-chesspuzzle")
    public ResponseEntity<?> getRandomChessPuzzle() {
        try {
            List<ChessPuzzle> chessPuzzles = chessPuzzleService.findAll();

            if (chessPuzzles.isEmpty()) {
                return new ResponseEntity<>("No chess puzzles available", HttpStatus.NOT_FOUND);
            }

            Random random = new Random();
            int randomIndex = random.nextInt(chessPuzzles.size());

            // Get ChessPuzzle ID at the random index
            Long randomChessPuzzleId = chessPuzzles.get(randomIndex).getId();

            ChessPuzzle randomChessPuzzle = chessPuzzleService.findById(randomChessPuzzleId)
                    .orElseThrow(() -> new EntityNotFoundException("ChessPuzzle with ID " + randomChessPuzzleId + " not found"));

            return new ResponseEntity<>(randomChessPuzzle, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving random chess puzzle", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //before using this method, first use StockfishController method "checkIfBestMove"
    // -> if that check passes, use this method and provide answer boolean true, appUserId and ChesspuzzleId
    // maybe only use method if answer is correct and might be altered that answer is then always true
    // before @RequestParam String appUserId, @RequestParam String chessPuzzleId
    @PostMapping("/process-answer")
    public ResponseEntity<?> processChessPuzzleResult(@RequestBody ProcessChessPuzzleDTO processChessPuzzleDTO) {
        String chessPuzzleId = processChessPuzzleDTO.getChessPuzzleId();
        String appUserId = processChessPuzzleDTO.getAppUserId();

        Long chessIdLong;
        if (!(expertTrophyService.findById(appUserId).isPresent())) {
            //there is not yet any trophy for given user so create one
            AppUser appUser = appUserRepository.findById(appUserId).get();
            chessIdLong = Long.parseLong(chessPuzzleId);
            ExpertTrophy expertTrophy = new ExpertTrophy(appUser);

            //add Chesspuzzle to Set (of solved puzzles)
            Set<ChessPuzzle> set = expertTrophy.getChessPuzzles();
            ChessPuzzle chessPuzzle = chessPuzzleService.findById(chessIdLong).get();
            set.add(chessPuzzle);

            expertTrophyService.addExpertTrophy(expertTrophy);

            return new ResponseEntity<>("new ExpertTrophy created and saved puzzle in there", HttpStatus.CREATED);
        }
        // expertTrophy already does exist so
        //now check if true then add to Set if not same puzzle Id already in Set!

        ExpertTrophy expertTrophy = expertTrophyService.findById(appUserId).get();
        Set<ChessPuzzle> set = expertTrophy.getChessPuzzles();
        chessIdLong = Long.parseLong(chessPuzzleId);
        ChessPuzzle chessPuzzle = chessPuzzleService.findById(chessIdLong).get();


        if (!(set.contains(chessPuzzle))) {
            //chessPuzzle is not yet in SET because not yet solved or correct

            set.add(chessPuzzle);

            System.out.println(set);

            expertTrophy.setChessPuzzles(set);
            expertTrophyService.deleteById(appUserId);
            expertTrophyService.addExpertTrophy(expertTrophy);
            if (set.size() >= 3) {
                //three have been solved alter status
                expertTrophy.setExpertStatus(ExpertStatus.EXPERT);
                expertTrophyService.deleteById(appUserId);
                expertTrophyService.addExpertTrophy(expertTrophy);
                //do i need to delete and re-add here now?
            }
        }
        return new ResponseEntity<>("existing ExpertTrophy and saved puzzle in there", HttpStatus.CREATED);

//        return new ResponseEntity<>("Either Answer wrong or Chesspuzzle already played", HttpStatus.BAD_REQUEST);
    }
}
