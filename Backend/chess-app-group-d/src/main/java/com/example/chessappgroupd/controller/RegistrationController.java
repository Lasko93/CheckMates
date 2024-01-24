package com.example.chessappgroupd.controller;
import com.example.chessappgroupd.domain.appUser.RegistrationRequest;
import com.example.chessappgroupd.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
//RegistrationController
public class RegistrationController {
    private final RegistrationService registrationService;
    //registers a User into the CheckMates system
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        try{
            return ResponseEntity.ok().body(registrationService.register(request));
        }catch (ResponseStatusException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
