package com.medical.service;

import com.medical.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Service for JWT token generation and validation.
 * Uses HS256 algorithm with configurable secret and expiration.
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;

    /**
     * Generates a signed JWT token containing userId, username, and role claims.
     *
     * @param userId   the user's ID
     * @param username the user's username
     * @param role     the user's role
     * @return a signed JWT string
     */
    public String generateToken(Long userId, String username, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(userId.toString())
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtConfig.getExpiration()))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validates a JWT token and returns its claims.
     *
     * @param token the JWT string to validate
     * @return the claims contained in the token
     * @throws ExpiredJwtException     if the token is expired
     * @throws MalformedJwtException   if the token is malformed
     * @throws SignatureException      if the token's signature is invalid
     * @throws IllegalArgumentException if the token is null or empty
     */
    public Claims validateToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token must not be null");
        }
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Derives an HMAC-SHA key from the configured secret string.
     *
     * @return a SecretKey suitable for HS256
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
