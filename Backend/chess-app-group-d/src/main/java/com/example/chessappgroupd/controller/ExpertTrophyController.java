package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.chesspuzzle.ChessPuzzle;
import com.example.chessappgroupd.domain.chesspuzzle.ExpertTrophy;
import com.example.chessappgroupd.service.ExpertTrophyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//This controller provides info on who owns status Expert an Noob, this controller might be used in order to switch status Noob to Expert
//also when chesspuzzle played shall get noted here


//This controller provides info on who owns status Expert an Noob, this controller might be used in order to switch status Noob to Expert
//also when chesspuzzle played shall get noted here
@RestController
@RequestMapping(path = "/expert-trophy")
@CrossOrigin(origins = "http://localhost:3000")
public class ExpertTrophyController {

    @Autowired
    private ExpertTrophyService expertTrophyService;

    @GetMapping("/find-all")
    public ResponseEntity<List<ExpertTrophy>> allExpertTrophies() {
        return new ResponseEntity<>(expertTrophyService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<?> findByID(@PathVariable("id") String id) {
        try {
            ExpertTrophy expertTrophy = expertTrophyService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("ExpertTrophy with ID " + id + " not found"));
            return new ResponseEntity<>(expertTrophy, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("ExpertTrophy with ID " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
        expertTrophyService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteAll() {
        expertTrophyService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
