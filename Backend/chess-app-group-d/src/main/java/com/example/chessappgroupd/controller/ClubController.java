package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.club.ClubRequest;
import com.example.chessappgroupd.domain.club.ClubDTO;
import com.example.chessappgroupd.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/club")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ClubController {
//  Ein Benutzer kann einen Schachclub mit einem eindeutigen Namen erstellen. Jeder Nutzer kann die
//  Liste aller erstellten Schachclubs einsehen. Weiterhin ist jeder Benutzer in der Lage einen Schachclub
//  beizutreten. Nutzer innerhalb eines Schachclubs können miteinander über einen Chat in Echtzeit
//  kommunizieren (siehe Chat). Außerdem soll im Profil eines jeden Nutzers ersichtlich sein, in welchen
//  Schachclubs er beigetreten ist.
    private final ClubService clubService;
    //find all clubs
    @GetMapping("/")
    public ResponseEntity<List<ClubDTO>> findAll() {
        return ResponseEntity.ok().body(clubService.findAll());

    }
    //find all club members
    @GetMapping("{clubName}/members/")
    public ResponseEntity<?> findAllMembersByClubName(@PathVariable String clubName) {
        try {
            return ResponseEntity.ok().body(clubService.findAllMembersByClubName(clubName));
        }catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //find club by clubname
    @GetMapping("/{clubName}")
    public ResponseEntity<?> findByClubName(@PathVariable String clubName) {
        try {
            return ResponseEntity.ok().body(clubService.findByClubName(clubName));
        }catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //find club by member username
    @GetMapping("/members/{userName}")
    public ResponseEntity<?> findByMember(@PathVariable String userName) {
        try {
            return ResponseEntity.ok().body(clubService.findByMember(userName));
        }catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //delete club by clubname
    @DeleteMapping("/{clubName}/delete")
    public ResponseEntity<?> deleteByClubName(@PathVariable String clubName) {
        try {
            return ResponseEntity.ok().body(clubService.deleteByClubName(clubName));
        }catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //delete club by member username
    @DeleteMapping("/members/{userName}/delete")
    public ResponseEntity<?> deleteByMember(@PathVariable String userName) {
        try {
            return ResponseEntity.ok().body(clubService.deleteByMember(userName));
        }catch (ResponseStatusException | UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //join club (automatisches austreten aus dem alten club oder es wird ein neues erstellt neuen falls noch nirgends drin)
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody ClubRequest clubRequest) {
        try{
            return ResponseEntity.ok().body(clubService.join(clubRequest));
        } catch (ResponseStatusException | UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //leave club
    @PostMapping("/leave")
    public ResponseEntity<?> leave(@RequestBody ClubRequest clubRequest) {
        try{
            return ResponseEntity.ok().body(clubService.leave(clubRequest.clubName(), clubRequest.userName()));
        } catch (ResponseStatusException | UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }












}