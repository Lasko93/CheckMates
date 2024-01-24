package com.example.chessappgroupd.service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
@RequiredArgsConstructor
//AuthService
//contains a method to validate/authenticate a User
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private static final String INVALID_AUTHENTICATION= "Invalid username or password!";
    //authenticates if userName or password is right:
    //validates a User by correct username (id) and password and returns a token
    public Authentication authenticateUser(String userName, String password) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_AUTHENTICATION);
        }
    }
}