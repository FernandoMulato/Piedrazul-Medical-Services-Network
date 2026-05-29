package com.medical.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * Global filter that enforces coarse-grained role-based access control.
 * Checks the X-User-Role header (set by JwtAuthFilter) against a route→role matrix.
 * Order = 200 (executes after JwtAuthFilter).
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@Component
public class RoleAuthorizationFilter implements GlobalFilter, Ordered {

    private static final String USER_ROLE_HEADER = "X-User-Role";

    private final List<RouteRule> rules;

    public RoleAuthorizationFilter() {
        this.rules = buildRules();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String role = exchange.getRequest().getHeaders().getFirst(USER_ROLE_HEADER);

        if (role == null || role.isBlank()) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        String path = exchange.getRequest().getURI().getPath();

        boolean allowed = rules.stream()
                .filter(rule -> pathMatches(path, rule.pathPrefix))
                .findFirst()
                .map(rule -> rule.allowedRoles.contains(role))
                .orElse(false);

        if (allowed) {
            return chain.filter(exchange);
        }

        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return 200;
    }

    /**
     * Checks if the given path matches the rule prefix.
     * Rules ending with "/" are prefix matches (e.g., "/api/users/" matches "/api/users/5").
     * Rules without trailing "/" are exact matches (e.g., "/api/users" only matches "/api/users").
     */
    private boolean pathMatches(String path, String pathPrefix) {
        if (pathPrefix.endsWith("/")) {
            return path.startsWith(pathPrefix);
        }
        return path.equals(pathPrefix);
    }

    private List<RouteRule> buildRules() {
        return List.of(
                // ── Users routes ──
                // Exact list (GET /api/users) — ADMIN only
                new RouteRule("/api/users", Set.of("ADMIN")),
                // Patient validation — ADMIN and PROFESSIONAL (more specific first)
                new RouteRule("/api/users/patients/validate/", Set.of("ADMIN", "PROFESSIONAL")),
                // Search — ADMIN only
                new RouteRule("/api/users/search/", Set.of("ADMIN")),
                // By-ID routes — ADMIN and PROFESSIONAL (fallback prefix)
                new RouteRule("/api/users/", Set.of("ADMIN", "PROFESSIONAL")),

                // ── Appointments ──
                new RouteRule("/api/appointments", Set.of("ADMIN", "SCHEDULER", "PROFESSIONAL", "PATIENT")),
                new RouteRule("/api/appointments/", Set.of("ADMIN", "SCHEDULER", "PROFESSIONAL", "PATIENT")),

                // ── Professionals ──
                new RouteRule("/api/professionals", Set.of("ADMIN", "SCHEDULER", "PROFESSIONAL", "PATIENT")),
                new RouteRule("/api/professionals/", Set.of("ADMIN", "SCHEDULER", "PROFESSIONAL", "PATIENT")),

                // ── Clinical records ──
                new RouteRule("/api/clinical-records", Set.of("ADMIN", "PROFESSIONAL")),
                new RouteRule("/api/clinical-records/", Set.of("ADMIN", "PROFESSIONAL")),

                // ── Reports ──
                new RouteRule("/api/reports", Set.of("ADMIN")),
                new RouteRule("/api/reports/", Set.of("ADMIN")),

                // ── Audits ──
                new RouteRule("/api/audits", Set.of("ADMIN")),
                new RouteRule("/api/audits/", Set.of("ADMIN"))
        );
    }

    /**
     * Internal record representing a route-role mapping rule.
     */
    private record RouteRule(String pathPrefix, Set<String> allowedRoles) {}
}
