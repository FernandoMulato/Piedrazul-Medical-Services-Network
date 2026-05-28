package com.medical.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;
import java.util.UUID;

/**
 * Custom ErrorAttributes that produces consistent JSON error responses
 * across all Gateway error scenarios (401, 403, 404, 503, etc.).
 * <p>
 * Adds the X-Correlation-Id header value (or generates one if missing)
 * to every error response for distributed tracing.
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);

        // Add or generate correlation ID for tracing
        String correlationId = request.headers().firstHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        errorAttributes.put("correlationId", correlationId);

        // Remove stack trace if present (never expose internals)
        errorAttributes.remove("trace");
        errorAttributes.remove("requestId");

        return errorAttributes;
    }
}
