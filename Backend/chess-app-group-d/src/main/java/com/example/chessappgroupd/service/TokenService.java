package com.example.chessappgroupd.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
//TokenService
//Contains methods for generating, validating and decompose a Token
//These methods return an AuthCode
public class TokenService {
    @Value("${app.security.secret-token-key}")
    private String SECRET_TOKEN_KEY;
    //generates a Token with a User
    public String generateToken(UserDetails userDetails) {
      return generateToken(new HashMap<>(), userDetails);
    }
    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //1d = 1000ms * 60 * 60 * 24
                .signWith(getSigninKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    //extracts a claim of a token
    private <T> T extractClaim(String token, Function<Claims,T> claimsResolve) {
        final Claims claims =extractAllClaims(token);
        return claimsResolve.apply(claims);
    }
    //gets key from SECRET_TOKEN_KEY for token generating
    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_TOKEN_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    //extracts all claims of a token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    //extracts the username of a token
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }
    //validates a token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    //validates a token expiration
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    //extracts the expiration of a token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}