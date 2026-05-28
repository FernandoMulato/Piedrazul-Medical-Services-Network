package com.medical.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD for GlobalErrorAttributes:
 * RED → test written first (GlobalErrorAttributes does not exist yet)
 * GREEN → minimum implementation
 * TRIANGULATE → correlationId present, correlationId missing, different status codes
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
class GlobalErrorAttributesTest {

    private GlobalErrorAttributes errorAttributes;

    @BeforeEach
    void setUp() {
        errorAttributes = new GlobalErrorAttributes();
    }

    @Test
    void shouldIncludeCorrelationId_whenPresent() {
        Map<String, Object> attrs = buildErrorAttributes("/api/users/123", "test-correlation-uuid");

        assertNotNull(attrs);
        assertEquals("test-correlation-uuid", attrs.get("correlationId"));
    }

    @Test
    void shouldIncludeAllRequiredFields() {
        Map<String, Object> attrs = buildErrorAttributes("/api/users/123", "corr-123");

        assertNotNull(attrs.get("status"), "status must be present");
        assertNotNull(attrs.get("error"), "error must be present");
        assertNotNull(attrs.get("path"), "path must be present");
        assertNotNull(attrs.get("timestamp"), "timestamp must be present");
        assertNotNull(attrs.get("correlationId"), "correlationId must be present");
    }

    @Test
    void shouldGenerateCorrelationId_whenMissing() {
        Map<String, Object> attrs = buildErrorAttributes("/api/unknown", null);

        assertNotNull(attrs);
        // When correlation ID header is missing, the error handler generates a UUID
        assertNotNull(attrs.get("correlationId"));
        assertFalse(((String) attrs.get("correlationId")).isBlank());
    }

    @Test
    void shouldContainRequestPath() {
        Map<String, Object> attrs = buildErrorAttributes("/api/appointments", "corr-456");

        assertEquals("/api/appointments", attrs.get("path"));
    }

    @Test
    void shouldNotContainStackTrace() {
        Map<String, Object> attrs = buildErrorAttributes("/api/users", "corr-789");

        assertFalse(attrs.containsKey("trace"), "stack trace must not be exposed");
    }

    /**
     * Helper to build error attributes by setting up the mock request exchange
     * with the required error attribute that DefaultErrorAttributes expects.
     */
    private Map<String, Object> buildErrorAttributes(String path, String correlationId) {
        MockServerHttpRequest.BaseBuilder<?> requestBuilder = MockServerHttpRequest.get(path);
        if (correlationId != null) {
            requestBuilder.header("X-Correlation-Id", correlationId);
        }
        var httpRequest = requestBuilder.build();
        var exchange = MockServerWebExchange.from(httpRequest);

        // DefaultErrorAttributes requires the exception to be stored in the exchange
        exchange.getAttributes().put(
                "org.springframework.boot.web.reactive.error.DefaultErrorAttributes.ERROR",
                new RuntimeException("test error")
        );

        var serverRequest = ServerRequest.create(exchange, List.of());
        return errorAttributes.getErrorAttributes(serverRequest, ErrorAttributeOptions.defaults());
    }
}
