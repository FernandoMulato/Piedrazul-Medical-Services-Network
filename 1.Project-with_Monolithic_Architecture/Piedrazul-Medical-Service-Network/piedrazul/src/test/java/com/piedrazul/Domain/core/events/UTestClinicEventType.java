package com.piedrazul.Domain.core.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link ClinicEventType} enum.
 */
class UTestClinicEventType {

  @Test
  @DisplayName("USER_CHANGE enum value exists and has correct name (normal case)")
  void userChangeEnumValueExists() {
    // Arrange & Act
    ClinicEventType type = ClinicEventType.USER_CHANGE;

    // Assert
    assertNotNull(type);
    assertEquals("USER_CHANGE", type.name());
  }

  @Test
  @DisplayName("STATUS_MESSAGE enum value exists and has correct name (normal case)")
  void statusMessageEnumValueExists() {
    // Arrange & Act
    ClinicEventType type = ClinicEventType.STATUS_MESSAGE;

    // Assert
    assertNotNull(type);
    assertEquals("STATUS_MESSAGE", type.name());
  }

  @Test
  @DisplayName("valueOf returns correct enum for USER_CHANGE string (normal case)")
  void valueOfReturnsUserChangeForValidString() {
    // Arrange
    String typeName = "USER_CHANGE";

    // Act
    ClinicEventType type = ClinicEventType.valueOf(typeName);

    // Assert
    assertEquals(ClinicEventType.USER_CHANGE, type);
  }

  @Test
  @DisplayName("valueOf returns correct enum for STATUS_MESSAGE string (normal case)")
  void valueOfReturnsStatusMessageForValidString() {
    // Arrange
    String typeName = "STATUS_MESSAGE";

    // Act
    ClinicEventType type = ClinicEventType.valueOf(typeName);

    // Assert
    assertEquals(ClinicEventType.STATUS_MESSAGE, type);
  }

  @Test
  @DisplayName("valueOf with invalid name throws IllegalArgumentException (error case)")
  void valueOfThrowsForInvalidName() {
    // Arrange
    String invalidName = "INVALID_TYPE";

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> ClinicEventType.valueOf(invalidName));
  }

  @Test
  @DisplayName("values returns array with exactly two elements (borderline case)")
  void valuesReturnsAllEnumConstants() {
    // Act
    ClinicEventType[] values = ClinicEventType.values();

    // Assert
    assertNotNull(values);
    assertEquals(2, values.length);
    assertEquals(ClinicEventType.USER_CHANGE, values[0]);
    assertEquals(ClinicEventType.STATUS_MESSAGE, values[1]);
  }
}
