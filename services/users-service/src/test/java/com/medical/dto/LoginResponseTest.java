package com.medical.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for LoginResponse DTO — verifies builder and field access.
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
class LoginResponseTest {

  @Test
  void shouldCreateLoginResponse_whenUsingBuilder() {
    // Given — build a LoginResponse with all fields
    LoginResponse response = LoginResponse.builder()
        .userId(1L)
        .username("jdoe")
        .role("PATIENT")
        .build();

    // Then — all fields should match
    assertNotNull(response);
    assertEquals(1L, response.getUserId());
    assertEquals("jdoe", response.getUsername());
    assertEquals("PATIENT", response.getRole());
  }

  @Test
  void shouldCreateLoginResponse_whenUsingNoArgsConstructor() {
    // Given — create empty then set fields
    LoginResponse response = new LoginResponse();
    response.setUserId(2L);
    response.setUsername("admin");
    response.setRole("ADMIN");

    // Then — fields should match
    assertEquals(2L, response.getUserId());
    assertEquals("admin", response.getUsername());
    assertEquals("ADMIN", response.getRole());
  }

  @Test
  void shouldCreateLoginResponse_whenUsingAllArgsConstructor() {
    // Given — create with all fields
    LoginResponse response = new LoginResponse(3L, "scheduler", "SCHEDULER");

    // Then — fields should match
    assertEquals(3L, response.getUserId());
    assertEquals("scheduler", response.getUsername());
    assertEquals("SCHEDULER", response.getRole());
  }

  @Test
  void shouldReturnNullFields_whenNotSet() {
    // Given — empty LoginResponse
    LoginResponse response = new LoginResponse();

    // Then — all fields should be null
    assertNull(response.getUserId());
    assertNull(response.getUsername());
    assertNull(response.getRole());
  }
}
