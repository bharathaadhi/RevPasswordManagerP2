package com.rev.revpasswordmanagerp2.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // 🔐 SECRET KEY (must be long enough)
    private final SecretKey key =
            Keys.hmacShaKeyFor(
                    "revPasswordManagerSecretKeyrevPasswordManagerSecretKey123"
                            .getBytes()
            );
    public String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username) // 👈 CORRECT for 0.11.5
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public String extractUsername(String token) {

        Claims claims = Jwts.parserBuilder()   // 👈 NOT parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
