package com.example.gymapp.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRETKEY = "Verdugvybiuinzorviozvnioziovnazoivnoiznvzivrzoirnviozrvoizvot2004";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(SECRETKEY)
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 heures
                .signWith(SignatureAlgorithm.HS256, SECRETKEY)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 jours
                .signWith(SignatureAlgorithm.HS256, SECRETKEY)
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            System.out.println("📢 Username extrait du token: " + extractedUsername);
            System.out.println("✅ Username attendu: " + username);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la validation du token: " + e.getMessage());
            return false;
        }
    }
    
    private Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }


    


}

