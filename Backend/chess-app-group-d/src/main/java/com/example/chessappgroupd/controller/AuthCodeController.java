package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.appUser.AuthCodeRequest;
import com.example.chessappgroupd.service.AuthCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


//controller for login,registration and authentication
@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
//AuthCodeController
public class AuthCodeController {
    private final AuthCodeService authCodeService;
    //validates 2fa codes
    @PostMapping("/verifyAuthCode")
    public ResponseEntity<?> verify2FACode(@RequestBody AuthCodeRequest codeRequest) {
        try{
            return ResponseEntity.ok(authCodeService.verifyAuthCode(codeRequest));
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
