package com.example.chessappgroupd.controller;
import com.example.chessappgroupd.service.ProfilePictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/photos")
@RequiredArgsConstructor
//ProfilePictureController
//Contains methods for downloading, uploading and deleting a ProfilePicture
public class ProfilePictureController {
    private final ProfilePictureService profilePictureService;
    //downloads an ProfilePicture by username
    @GetMapping("/{userName}")
    public ResponseEntity<?> download(@PathVariable("userName") String userName) {
        try {
            return ResponseEntity.ok().body(profilePictureService.findProfilePicByUser(userName));
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    //uploads an ProfilePicture by username
    @PostMapping("/")
    public ResponseEntity<?> upload(@RequestParam("image") MultipartFile file, @RequestParam("userName") String userName) {
        try {
            return ResponseEntity.ok().body(profilePictureService.saveProfilePic(file, userName));
        } catch (UsernameNotFoundException | IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    //deletes an ProfilePicture by username
    @DeleteMapping("/delete/{userName}")
    public ResponseEntity<?> deleteByUserName(@PathVariable("userName") String userName) {
        try {
            profilePictureService.deleteProfilePicByUser(userName);
            return ResponseEntity.ok().body("User with username: '" + userName + "' was successfully deleted!");
        } catch (UsernameNotFoundException | FileNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
