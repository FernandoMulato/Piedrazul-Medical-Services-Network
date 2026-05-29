package com.medical.service;

import com.medical.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD cycle for JwtService:
 * RED → test written first (JwtService does not exist yet)
 * GREEN → minimum implementation
 * TRIANGULATE → expired, malformed, wrong signature
 * REFACTOR → clean code with tests passing
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
class JwtServiceTest {

    // Secrets must be ≥64 chars (512 bits) for jjwt HS512 default
    private static final String SECRET = "my-test-secret-key-that-is-at-least-256-bits-long-for-hs256-512-bit-key!!";
    private static final long EXPIRATION_MS = 3600000L; // 1 hour

    private JwtConfig jwtConfig;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtConfig = new JwtConfig();
        jwtConfig.setSecret(SECRET);
        jwtConfig.setExpiration(EXPIRATION_MS);
        jwtService = new JwtService(jwtConfig);
    }

    @Nested
    class GenerateToken {

        @Test
        void shouldGenerateValidToken_whenGivenValidInputs() {
            String token = jwtService.generateToken(1L, "jdoe", "ADMIN");

            assertNotNull(token);
            assertFalse(token.isBlank());
            // Verify it can be parsed back
            assertDoesNotThrow(() -> jwtService.validateToken(token));
        }

        @Test
        void shouldContainCorrectClaimsInToken_whenGenerated() {
            String token = jwtService.generateToken(42L, "alice", "PROFESSIONAL");
            Claims claims = jwtService.validateToken(token);

            assertEquals("42", claims.getSubject());
            assertEquals("alice", claims.get("username"));
            assertEquals("PROFESSIONAL", claims.get("role"));
            assertNotNull(claims.getIssuedAt());
            assertNotNull(claims.getExpiration());
        }

        @Test
        void shouldGenerateDifferentTokens_whenDifferentUsers() {
            String token1 = jwtService.generateToken(1L, "user1", "PATIENT");
            String token2 = jwtService.generateToken(2L, "user2", "ADMIN");

            assertNotEquals(token1, token2);
        }
    }

    @Nested
    class ValidateToken {

        @Test
        void shouldReturnClaims_whenTokenIsValid() {
            String token = jwtService.generateToken(1L, "jdoe", "ADMIN");
            Claims claims = jwtService.validateToken(token);

            assertNotNull(claims);
            assertEquals("1", claims.getSubject());
        }

        @Test
        void shouldThrowExpiredJwtException_whenTokenIsExpired() {
            // Create a service with 0 expiration to force immediate expiry
            JwtConfig expiredConfig = new JwtConfig();
            expiredConfig.setSecret(SECRET);
            expiredConfig.setExpiration(-1000L); // already expired
            JwtService expiredService = new JwtService(expiredConfig);

            String token = expiredService.generateToken(1L, "jdoe", "ADMIN");

            assertThrows(ExpiredJwtException.class, () -> expiredService.validateToken(token));
        }

        @Test
        void shouldThrowMalformedJwtException_whenTokenIsMalformed() {
            assertThrows(MalformedJwtException.class, () -> jwtService.validateToken("not-a-valid-jwt-token"));
        }

        @Test
        void shouldThrowSignatureException_whenTokenHasWrongSignature() {
            // Create a token with a different secret
            JwtConfig otherConfig = new JwtConfig();
            otherConfig.setSecret("a-different-secret-key-that-is-also-at-least-256-bits-long-for-hs512-other!!");
            otherConfig.setExpiration(EXPIRATION_MS);
            JwtService otherService = new JwtService(otherConfig);

            String token = otherService.generateToken(1L, "jdoe", "ADMIN");

            assertThrows(SignatureException.class, () -> jwtService.validateToken(token));
        }

        @Test
        void shouldThrowException_whenTokenIsNull() {
            assertThrows(IllegalArgumentException.class, () -> jwtService.validateToken(null));
        }

        @Test
        void shouldThrowException_whenTokenIsEmpty() {
            // jjwt 0.12.x throws IllegalArgumentException for empty/blank strings
            assertThrows(IllegalArgumentException.class, () -> jwtService.validateToken(""));
        }
    }
}
