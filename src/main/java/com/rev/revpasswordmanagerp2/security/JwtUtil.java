package com.rev.revpasswordmanagerp2.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET =
            "revPasswordManagerSecretKeyrevPasswordManagerSecretKey123";

    // ======================================
    // ✅ GENERATE TOKEN
    // ======================================
    public String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username)        // ✅ CORRECT for 0.11.5
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    // ======================================
    // ✅ EXTRACT USERNAME
    // ======================================
    public String extractUsername(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
