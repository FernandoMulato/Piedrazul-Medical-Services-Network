package com.medical.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for LoginRequest DTO — validates field constraints.
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
class LoginRequestTest {

  private static ValidatorFactory validatorFactory;
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @AfterAll
  static void tearDown() {
    if (validatorFactory != null) {
      validatorFactory.close();
    }
  }

  @Test
  void shouldCreateLoginRequest_whenValidFields() {
    // Given — a valid LoginRequest with both username and password
    LoginRequest request = new LoginRequest("jdoe", "correct-password");

    // When — we validate
    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

    // Then — no constraint violations
    assertTrue(violations.isEmpty(), "Valid LoginRequest should have no violations");
    assertEquals("jdoe", request.getUsername());
    assertEquals("correct-password", request.getPassword());
  }

  @Test
  void shouldFailValidation_whenUsernameIsBlank() {
    // Given — a LoginRequest with blank username
    LoginRequest request = new LoginRequest("", "password123");

    // When — we validate
    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

    // Then — there should be a violation on username
    assertFalse(violations.isEmpty(), "Blank username should trigger validation");
    boolean hasUsernameViolation = violations.stream()
        .anyMatch(v -> "username".equals(v.getPropertyPath().toString()));
    assertTrue(hasUsernameViolation, "Violation should be on 'username' field");
  }

  @Test
  void shouldFailValidation_whenPasswordIsBlank() {
    // Given — a LoginRequest with blank password
    LoginRequest request = new LoginRequest("jdoe", "");

    // When — we validate
    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

    // Then — there should be a violation on password
    assertFalse(violations.isEmpty(), "Blank password should trigger validation");
    boolean hasPasswordViolation = violations.stream()
        .anyMatch(v -> "password".equals(v.getPropertyPath().toString()));
    assertTrue(hasPasswordViolation, "Violation should be on 'password' field");
  }

  @Test
  void shouldFailValidation_whenBothFieldsAreBlank() {
    // Given — a LoginRequest with both fields blank
    LoginRequest request = new LoginRequest("", "");

    // When — we validate
    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

    // Then — there should be violations for both fields
    assertEquals(2, violations.size(), "Both blank fields should trigger violations");
  }
}
