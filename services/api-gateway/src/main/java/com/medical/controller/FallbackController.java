package com.medical.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Controller that provides circuit breaker fallback endpoints.
 * Returns 503 Service Unavailable with a descriptive message when
 * a downstream service is unreachable or the circuit is open.
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@RestController
public class FallbackController {

    @GetMapping("/fallback/users")
    public Mono<ResponseEntity<Map<String, Object>>> usersFallback() {
        return fallbackResponse("Users service unavailable. Please try again later.");
    }

    @GetMapping("/fallback/appointments")
    public Mono<ResponseEntity<Map<String, Object>>> appointmentsFallback() {
        return fallbackResponse("Appointments service unavailable. Please try again later.");
    }

    @GetMapping("/fallback/professionals")
    public Mono<ResponseEntity<Map<String, Object>>> professionalsFallback() {
        return fallbackResponse("Professionals service unavailable. Please try again later.");
    }

    @GetMapping("/fallback/clinical-records")
    public Mono<ResponseEntity<Map<String, Object>>> clinicalRecordsFallback() {
        return fallbackResponse("Clinical records service unavailable. Please try again later.");
    }

    @GetMapping("/fallback/reports")
    public Mono<ResponseEntity<Map<String, Object>>> reportsFallback() {
        return fallbackResponse("Reports service unavailable. Please try again later.");
    }

    @GetMapping("/fallback/audits")
    public Mono<ResponseEntity<Map<String, Object>>> auditsFallback() {
        return fallbackResponse("Audits service unavailable. Please try again later.");
    }

    private Mono<ResponseEntity<Map<String, Object>>> fallbackResponse(String message) {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "status", 503,
                        "error", "Service Unavailable",
                        "message", message
                )));
    }
}
