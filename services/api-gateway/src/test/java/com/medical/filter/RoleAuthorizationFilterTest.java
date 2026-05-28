package com.medical.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TDD for RoleAuthorizationFilter:
 * RED → test written first (RoleAuthorizationFilter does not exist yet)
 * GREEN → minimum implementation
 * TRIANGULATE → multiple route/role combinations
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
class RoleAuthorizationFilterTest {

    private final RoleAuthorizationFilter filter = new RoleAuthorizationFilter();
    private final GatewayFilterChain chain = mock(GatewayFilterChain.class);

    @BeforeEach
    void setUp() {
        when(chain.filter(any())).thenReturn(Mono.empty());
    }

    @Nested
    class AdminAccess {

        @Test
        void shouldAllowAdminToAccessUsersRoute() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/users")
                            .header("X-User-Role", "ADMIN")
                            .build());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(chain).filter(any());
        }

        @Test
        void shouldAllowAdminToAccessReportsRoute() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/reports/daily")
                            .header("X-User-Role", "ADMIN")
                            .build());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(chain).filter(any());
        }

        @Test
        void shouldAllowAdminToAccessAuditsRoute() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/audits/log")
                            .header("X-User-Role", "ADMIN")
                            .build());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(chain).filter(any());
        }
    }

    @Nested
    class PatientBlocked {

        @Test
        void shouldBlockPatientFromUsersRoute() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/users")
                            .header("X-User-Role", "PATIENT")
                            .build());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(chain, never()).filter(any());
        }

        @Test
        void shouldAllowPatientToProfessionalsRoute() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/professionals")
                            .header("X-User-Role", "PATIENT")
                            .build());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(chain).filter(any());
        }

        @Test
        void shouldAllowPatientToAppointmentsRoute() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/appointments")
                            .header("X-User-Role", "PATIENT")
                            .build());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(chain).filter(any());
        }
    }

    @Nested
    class ProfessionalAccess {

        @Test
        void shouldBlockProfessionalFromReportsRoute() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/reports")
                            .header("X-User-Role", "PROFESSIONAL")
                            .build());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(chain, never()).filter(any());
        }

        @Test
        void shouldAllowProfessionalToClinicalRecordsRoute() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/clinical-records/5")
                            .header("X-User-Role", "PROFESSIONAL")
                            .build());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(chain).filter(any());
        }

        @Test
        void shouldAllowProfessionalToUserByIdRoute() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/users/5")
                            .header("X-User-Role", "PROFESSIONAL")
                            .build());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(chain).filter(any());
        }

        @Test
        void shouldBlockProfessionalFromUserListRoute() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/users")
                            .header("X-User-Role", "PROFESSIONAL")
                            .build());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(chain, never()).filter(any());
        }
    }

    @Nested
    class UnknownRole {

        @Test
        void shouldBlockUnknownRole() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/appointments")
                            .header("X-User-Role", "UNKNOWN_ROLE")
                            .build());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(chain, never()).filter(any());
        }
    }

    @Nested
    class MissingRoleHeader {

        @Test
        void shouldBlockRequestWithNoRoleHeader() {
            ServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/users").build());

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();

            verify(chain, never()).filter(any());
        }
    }
}
