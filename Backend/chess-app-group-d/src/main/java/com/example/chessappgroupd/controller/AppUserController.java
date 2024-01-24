package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.appUser.AppUserDTO;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.service.AppUserService;
import com.example.chessappgroupd.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
//AppUserController
//Contains classic find and delete methods for an AppUser
//These methods return an DTO of an AppUser in order to only send secure data to the frontend
public class AppUserController {
    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;
    @Value("${app.user.username.placeholder}")
    private String USER_PLACEHOLDER;
    //returns all registered Users as a List of AppUserDTO
    @GetMapping("/")
    public ResponseEntity<List<AppUserDTO>> findAll() {
        return ResponseEntity.ok().body(appUserService.findAll());
    }
    //returns an AppUserDTO by username
    @GetMapping("/{userName}")
    public ResponseEntity<?> findByUserName(@PathVariable("userName") String userName) {
        try {
            return ResponseEntity.ok().body(appUserService.findByUserName(userName));
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    //deletes an User by username
    @DeleteMapping("/delete/{userName}")
    public ResponseEntity<?> deleteByUserName(@PathVariable("userName") String userName) {
        if (appUserRepository.existsByUserName(userName)) {
            appUserService.deleteByUserName(userName);
            return userName.equals(USER_PLACEHOLDER)?
                    ResponseEntity.ok().body("User with username: '" + userName + "' have to stay in database! Don't delete this user!") :
                    ResponseEntity.ok().body("User with username: '" + userName + "' was successfully deleted!");
        }
        return new ResponseEntity<>("User with username: '" + userName + "' doesn't exists!", HttpStatus.NOT_FOUND);
    }
}
