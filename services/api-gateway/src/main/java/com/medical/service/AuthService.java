package com.medical.service;

import com.medical.dto.AuthValidationRequest;
import com.medical.dto.AuthValidationResponse;
import com.medical.dto.LoginRequest;
import com.medical.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service that orchestrates the login flow:
 * 1. Validates credentials against users-service via WebClient
 * 2. Generates a JWT using JwtService on successful validation
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final WebClient.Builder webClientBuilder;
    private final JwtService jwtService;

    /**
     * Authenticates user credentials and returns a signed JWT.
     * Delegates credential validation to users-service.
     *
     * @param request the login credentials
     * @return Mono containing LoginResponse with JWT, or error on 401
     */
    public Mono<LoginResponse> login(LoginRequest request) {
        WebClient client = webClientBuilder.baseUrl("http://localhost:8081").build();

        AuthValidationRequest validationRequest = new AuthValidationRequest(
                request.getUsername(), request.getPassword());

        return client.post()
                .uri("/api/users/auth/login")
                .body(Mono.just(validationRequest), AuthValidationRequest.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(
                                        new IllegalArgumentException("Invalid credentials: " + error))))
                .bodyToMono(AuthValidationResponse.class)
                .flatMap(validationResponse -> {
                    String token = jwtService.generateToken(
                            validationResponse.getUserId(),
                            validationResponse.getUsername(),
                            validationResponse.getRole());
                    return Mono.just(LoginResponse.builder()
                            .token(token)
                            .expiresIn(3600000L) // placeholder — will be read from config in PR 3
                            .build());
                });
    }
}
