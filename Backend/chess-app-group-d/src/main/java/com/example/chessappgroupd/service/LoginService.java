package com.example.chessappgroupd.service;
import com.example.chessappgroupd.domain.appUser.*;
import com.example.chessappgroupd.repository.AuthCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
@AllArgsConstructor
//LoginService
public class LoginService {
    private final AuthService authService;
    private final AuthCodeService authCodeService;
    private final AuthCodeRepository authCodeRepository;
    private static final String INVALID_LOGIN_DATA = "Invalid username or password!";
    //returns an AuthCode id when the login was successfully
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            authService.authenticateUser(loginRequest.userName(), loginRequest.password());//Are username and password correct?
        }catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_LOGIN_DATA);
        }
        var authenticationUserDetailsToken = authService.authenticateUser(loginRequest.userName(), loginRequest.password());//user is authenticated -> get username and password
        var user = (AppUser) authenticationUserDetailsToken.getPrincipal();//UserDetails are read from the token and stored in an AppUser object
        while (authCodeRepository.existsByUserName(user.getUsername())) {
            authCodeRepository.deleteByUserName(user.getUsername());
        }
        AuthCode authCode = authCodeService.sendAuthCode(user);
        return ResponseEntity.ok(new AuthCodeResponse(authCode.getId()));
    }
}