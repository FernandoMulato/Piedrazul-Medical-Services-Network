package com.medical.controller;

import com.medical.dto.LoginRequest;
import com.medical.dto.LoginResponse;
import com.medical.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * REST controller for authentication operations.
 * Exposes POST /auth/login for JWT token issuance.
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates user credentials and returns a signed JWT.
     *
     * @param request the login request with username and password
     * @return 200 with LoginResponse on success, 401 on invalid credentials
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(IllegalArgumentException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }
}
