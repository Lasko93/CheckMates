package com.example.chessappgroupd.service;
import com.example.chessappgroupd.domain.appUser.*;
import com.example.chessappgroupd.repository.AuthCodeRepository;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
//AppUserService
//Contains methods for generating and validating an AuthCode
//These methods return an AuthCode
//There is also a methode to send an AuthCode via Email
public class AuthCodeService {
    private static final int EXPIRY_TIME_IN_MINUTES = 30;
    private final AuthCodeRepository authCodeRepository;
    private final TokenService tokenService;
    private final JavaMailSender mailSender;
    @Value("${app.security.master-key}")
    private String MASTER_KEY;
    private final String INVALID_AUTH_CODE_ID = "Invalid authentication code ID";
    private final String INVALID_AUTH_CODE = "Invalid authenticationCode";
    //generates a 5 digit 2fa-code which expires after "EXPIRY_TIME_IN_MINUTES" minutes
    private AuthCode generateAuthCode(AppUser user) {
        AuthCode authCode = new AuthCode();
        String randomAuthCode = UUID.randomUUID().toString().substring(0, 5);
        authCode.setUser(user);
        authCode.setAuthCode(randomAuthCode);
        authCode.setExpiryTime(LocalDateTime.now().plusMinutes(EXPIRY_TIME_IN_MINUTES));
        authCodeRepository.save(authCode);
        return authCode;
    }
    //sends an email with authentication code to the user
    public AuthCode sendAuthCode(AppUser user) {
        SimpleMailMessage message = new SimpleMailMessage();
        AuthCode authCode = generateAuthCode(user);
        message.setFrom(Emails.CHECKMATES_MAIL_ADDRESS);
        message.setTo(user.getEmail());
        message.setSubject(Emails.AUTHCODE_EMAIL_SUBJECT);
        message.setText(String.format(Emails.AUTHCODE_EMAIL_TEXT, user.getUsername(), authCode.getAuthCode(), EXPIRY_TIME_IN_MINUTES));
        mailSender.send(message);
        return authCode;
    }
    //AppUser gets an authCode, this is checked with authCode in the DB and returns the TokenResponse
    public TokenResponse verifyAuthCode(@RequestBody AuthCodeRequest authCodeRequest) {
        var authCode = authCodeRepository.findById(authCodeRequest.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_AUTH_CODE_ID));
        if (!verifyCode(authCode, authCodeRequest.authCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_AUTH_CODE);
        }
        authCodeRepository.delete(authCode);
        AppUser appUser = authCode.getUser();
        var jwt = tokenService.generateToken(appUser);
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(jwt);
        return tokenResponse;
    }
    //AuthCode/MASTER_KEY of DB and authCode of AppUser are checked for agreement and expiryTime
    public boolean verifyCode(AuthCode authCode, String userAuthCode) {
        if (userAuthCode.equals(MASTER_KEY)) {
            return true;
        }
        return authCode.getAuthCode().equals(userAuthCode) && authCode.getExpiryTime().isAfter(LocalDateTime.now());
    }
}