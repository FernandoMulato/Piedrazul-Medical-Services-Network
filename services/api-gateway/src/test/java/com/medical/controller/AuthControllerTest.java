package com.medical.controller;

import com.medical.dto.LoginRequest;
import com.medical.dto.LoginResponse;
import com.medical.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * TDD for AuthController:
 * RED → test written first (AuthController does not exist yet)
 * GREEN → minimum implementation
 * TRIANGULATE → handle 401, handle validation errors
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@WebFluxTest(AuthController.class)
@Import(AuthControllerTest.TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AuthService authService;

    @Test
    void shouldReturn200AndToken_whenLoginIsSuccessful() {
        LoginResponse loginResponse = LoginResponse.builder()
                .token("jwt-token-here")
                .expiresIn(86400000L)
                .build();

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(Mono.just(loginResponse));

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"username\": \"jdoe\", \"password\": \"correct-password\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isEqualTo("jwt-token-here")
                .jsonPath("$.expiresIn").isEqualTo(86400000);
    }

    @Test
    void shouldReturn401_whenCredentialsAreInvalid() {
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(Mono.error(new IllegalArgumentException("Invalid credentials")));

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"username\": \"jdoe\", \"password\": \"wrong\"}")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldReturn400_whenRequestBodyIsInvalid() {
        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"username\": \"\"}")
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * Test security configuration: permit all requests for controller slice tests.
     */
    @org.springframework.boot.test.context.TestConfiguration
    static class TestSecurityConfig {

        @Bean
        SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
            http
                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())
                .csrf(csrf -> csrf.disable());
            return http.build();
        }
    }
}
