package com.medical.filter;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD for CorrelationIdFilter:
 * RED → test written first (CorrelationIdFilter does not exist yet)
 * GREEN → minimum implementation
 * TRIANGULATE → existing UUID preserved, missing UUID generated
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
class CorrelationIdFilterTest {

    private final CorrelationIdFilter filter = new CorrelationIdFilter();

    @Test
    void shouldGenerateCorrelationId_whenHeaderIsMissing() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/users").build());

        GatewayFilterChain chain = (ex) -> {
            String correlationId = ex.getRequest().getHeaders()
                    .getFirst("X-Correlation-Id");
            assertNotNull(correlationId, "X-Correlation-Id should be generated");
            assertTrue(correlationId.matches(
                    "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
            return Mono.empty();
        };

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();
    }

    @Test
    void shouldPreserveExistingCorrelationId_whenHeaderIsPresent() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/users")
                        .header("X-Correlation-Id", "abc-123-def")
                        .build());

        GatewayFilterChain chain = (ex) -> {
            String correlationId = ex.getRequest().getHeaders()
                    .getFirst("X-Correlation-Id");
            assertEquals("abc-123-def", correlationId,
                    "Existing X-Correlation-Id should be preserved");
            return Mono.empty();
        };

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();
    }
}
