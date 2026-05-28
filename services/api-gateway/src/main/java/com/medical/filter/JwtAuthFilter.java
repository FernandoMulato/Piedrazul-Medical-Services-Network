package com.medical.filter;

import com.medical.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Global filter that validates JWT tokens on protected routes.
 * Skips public paths (/auth/login, POST /api/users).
 * Sets X-User-Id and X-User-Role headers from validated JWT claims.
 * Order = 100 (executes after CorrelationIdFilter).
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        // Skip public paths
        if (isPublicPath(path, method)) {
            return chain.filter(exchange);
        }

        // Extract token
        String authHeader = exchange.getRequest().getHeaders()
                .getFirst(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        // Validate token
        try {
            Claims claims = jwtService.validateToken(token);

            // Set user headers
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", claims.getSubject())
                    .header("X-User-Role", claims.get("role", String.class))
                    .build();

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();

            return chain.filter(modifiedExchange);
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 100;
    }

    /**
     * Determines if the given path+method combination is public (no auth required).
     */
    private boolean isPublicPath(String path, String method) {
        if ("POST".equalsIgnoreCase(method) && "/auth/login".equals(path)) {
            return true;
        }
        if ("POST".equalsIgnoreCase(method) && "/api/users".equals(path)) {
            return true;
        }
        return false;
    }
}
