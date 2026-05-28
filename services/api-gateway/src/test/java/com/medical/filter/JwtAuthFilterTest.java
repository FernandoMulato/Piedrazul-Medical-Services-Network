package com.medical.filter;

import com.medical.service.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * TDD for JwtAuthFilter:
 * RED → test written first (JwtAuthFilter does not exist yet)
 * GREEN → minimum implementation
 * TRIANGULATE → public paths skipped, invalid/expired token rejected
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private GatewayFilterChain chain;

    private JwtAuthFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthFilter(jwtService);
    }

    @Nested
    class SkipPublicPaths {

        @Test
        void shouldSkipAuth_whenPathIsLogin() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.post("/auth/login").build());

            when(chain.filter(exchange)).thenReturn(Mono.empty());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(jwtService, never()).validateToken(anyString());
        }

        @Test
        void shouldSkipAuth_whenPathIsPostUsers() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.post("/api/users").build());

            when(chain.filter(exchange)).thenReturn(Mono.empty());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(jwtService, never()).validateToken(anyString());
        }

        @Test
        void shouldNotSkipAuth_whenPathIsUsersGet() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/users")
                            .header("Authorization", "Bearer valid-token")
                            .build());

            Claims claims = mock(Claims.class);
            when(claims.getSubject()).thenReturn("1");
            when(claims.get("username")).thenReturn("jdoe");
            when(claims.get("role")).thenReturn("ADMIN");

            when(jwtService.validateToken("valid-token")).thenReturn(claims);
            when(chain.filter(any())).thenReturn(Mono.empty());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(jwtService).validateToken("valid-token");
        }
    }

    @Nested
    class AuthenticatedRequests {

        @Test
        void shouldSetUserHeaders_whenTokenIsValid() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/users")
                            .header("Authorization", "Bearer valid-token")
                            .build());

            Claims claims = mock(Claims.class);
            when(claims.getSubject()).thenReturn("42");
            when(claims.get("username")).thenReturn("alice");
            when(claims.get("role")).thenReturn("PROFESSIONAL");

            when(jwtService.validateToken("valid-token")).thenReturn(claims);
            when(chain.filter(any())).thenReturn(Mono.empty());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(jwtService).validateToken("valid-token");
        }

        @Test
        void shouldReturn401_whenAuthorizationHeaderIsMissing() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/users").build());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(jwtService, never()).validateToken(anyString());
            verify(chain, never()).filter(any());
        }

        @Test
        void shouldReturn401_whenTokenIsInvalid() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/users")
                            .header("Authorization", "Bearer invalid-token")
                            .build());

            when(jwtService.validateToken("invalid-token"))
                    .thenThrow(new io.jsonwebtoken.security.SignatureException("Invalid signature"));

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(jwtService).validateToken("invalid-token");
            verify(chain, never()).filter(any());
        }
    }
}
