package org.olzhas.projectnic.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JWTService {

    @Value("${TOKEN_EXPIRATION_TIME}")
    private long TOKEN_EXPIRATION_TIME;

    @Value("${TOKEN_REFRESH_TIME}")
    private long TOKEN_REFRESH_TIME;

    private final SecretKey KEY = Jwts.SIG.HS512.key().build();

    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return exctractClaim(token, Claims::getSubject);
    }

    private <T> T exctractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = exctractAllClaim(token);
        return claimsResolvers.apply(claims);
    }

    private Claims exctractAllClaim(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return exctractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String generateRefreshToken(UserDetails userDetails) {

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("RoleType", userDetails.getAuthorities());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_REFRESH_TIME))
                .signWith(KEY)
                .compact();
    }
}
