package com.library.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private Key key;

    @PostConstruct
    public void initKey() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
   }

    public String generateJwtToken(Authentication authentication) {
        String username = authentication.getName();
        return generateJwtTokenFromUsername(username);
    }

    public String generateJwtTokenFromUsername(String username) {
        logger.info("key=", key);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            {
                logger.error("Invalid JWT token: {}", e.getMessage());
                return false;
            }
        }
    }

    public String extractUsername(String token) {
                try {
                return Jwts.parserBuilder().
                        setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
                } catch (Exception e) {
                    return null;
                }

    }
}
