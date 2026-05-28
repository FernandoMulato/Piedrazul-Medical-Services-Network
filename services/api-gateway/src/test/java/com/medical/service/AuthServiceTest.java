package com.medical.service;

import com.medical.dto.AuthValidationRequest;
import com.medical.dto.AuthValidationResponse;
import com.medical.dto.LoginRequest;
import com.medical.dto.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * TDD for AuthService:
 * RED → test written first (AuthService does not exist yet)
 * GREEN → minimum implementation
 * TRIANGULATE → handle 401, handle connection error
 * REFACTOR → clean code with tests passing
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        authService = new AuthService(webClientBuilder, jwtService);
    }

    @Nested
    class Login {

        private static final String USERS_SERVICE_URL = "http://localhost:8081";

        @Test
        void shouldReturnLoginResponse_whenCredentialsAreValid() {
            // Arrange
            LoginRequest request = new LoginRequest("jdoe", "correct-password");
            AuthValidationResponse validationResponse = AuthValidationResponse.builder()
                    .userId(1L)
                    .username("jdoe")
                    .role("ADMIN")
                    .build();

            when(jwtService.generateToken(1L, "jdoe", "ADMIN")).thenReturn("mocked-jwt-token");
            when(webClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri("/api/users/auth/login")).thenReturn(requestBodySpec);
            when(requestBodySpec.body(any(), eq(AuthValidationRequest.class))).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(AuthValidationResponse.class)).thenReturn(Mono.just(validationResponse));

            // Act
            Mono<LoginResponse> result = authService.login(request);

            // Assert
            StepVerifier.create(result)
                    .assertNext(response -> {
                        assertEquals("mocked-jwt-token", response.getToken());
                        assertNotNull(response.getExpiresIn());
                    })
                    .verifyComplete();

            verify(jwtService).generateToken(1L, "jdoe", "ADMIN");
        }

        @Test
        void shouldReturnErrorMono_whenCredentialsAreInvalid() {
            // Arrange
            LoginRequest request = new LoginRequest("jdoe", "wrong-password");

            when(webClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri("/api/users/auth/login")).thenReturn(requestBodySpec);
            when(requestBodySpec.body(any(), eq(AuthValidationRequest.class))).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            // Mock onStatus to return self (no-op), bodyToMono will trigger the error
            when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(AuthValidationResponse.class))
                    .thenReturn(Mono.error(new RuntimeException("401 Unauthorized")));

            // Act
            Mono<LoginResponse> result = authService.login(request);

            // Assert
            StepVerifier.create(result)
                    .expectError()
                    .verify();
        }
    }
}
