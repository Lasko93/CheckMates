package com.example.chessappgroupd.domain.appUser;
import lombok.Data;
@Data
//AuthCodeResponse
//represents the return value of verifyAuthCode(), (token)
public class TokenResponse {
    private String token;
}