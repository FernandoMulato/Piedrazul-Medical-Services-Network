package com.piedrazul.Domain.core.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link ClsClinicEvent}.
 */
class UTestClsClinicEvent {

  @Test
  @DisplayName("Constructor stores USER_CHANGE type and message correctly (normal case)")
  void testConstructorStoresUserChangeTypeAndMessageCorrectly() {
    // Arrange
    ClinicEventType expectedType = ClinicEventType.USER_CHANGE;
    String expectedMessage = "User profile updated";

    // Act
    ClsClinicEvent event = new ClsClinicEvent(expectedType, expectedMessage);

    // Assert
    assertNotNull(event);
    assertEquals(expectedType, event.getType());
    assertEquals(expectedMessage, event.getMessage());
  }

  @Test
  @DisplayName("Constructor stores STATUS_MESSAGE type and message correctly (normal case)")
  void testConstructorStoresStatusMessageTypeAndMessageCorrectly() {
    // Arrange
    ClinicEventType expectedType = ClinicEventType.STATUS_MESSAGE;
    String expectedMessage = "Operation completed successfully";

    // Act
    ClsClinicEvent event = new ClsClinicEvent(expectedType, expectedMessage);

    // Assert
    assertNotNull(event);
    assertEquals(expectedType, event.getType());
    assertEquals(expectedMessage, event.getMessage());
  }

  @Test
  @DisplayName("Constructor accepts empty message (borderline case)")
  void testConstructorAcceptsEmptyMessage() {
    // Arrange
    ClinicEventType type = ClinicEventType.USER_CHANGE;
    String emptyMessage = "";

    // Act
    ClsClinicEvent event = new ClsClinicEvent(type, emptyMessage);

    // Assert
    assertNotNull(event);
    assertEquals(type, event.getType());
    assertEquals(emptyMessage, event.getMessage());
  }

  @Test
  @DisplayName("Constructor accepts null message (borderline case)")
  void testConstructorAcceptsNullMessage() {
    // Arrange
    ClinicEventType type = ClinicEventType.STATUS_MESSAGE;

    // Act
    ClsClinicEvent event = new ClsClinicEvent(type, null);

    // Assert
    assertNotNull(event);
    assertEquals(type, event.getType());
    assertEquals(null, event.getMessage());
  }

  @Test
  @DisplayName("Constructor accepts long message (borderline case)")
  void testConstructorAcceptsLongMessage() {
    // Arrange
    ClinicEventType type = ClinicEventType.STATUS_MESSAGE;
    String longMessage = "A".repeat(10000);

    // Act
    ClsClinicEvent event = new ClsClinicEvent(type, longMessage);

    // Assert
    assertNotNull(event);
    assertEquals(type, event.getType());
    assertEquals(longMessage, event.getMessage());
  }

  @Test
  @DisplayName("Event is immutable - getters return same values as constructor args")
  void testEventIsImmutableGettersReturnConstructorValues() {
    // Arrange
    ClinicEventType type = ClinicEventType.USER_CHANGE;
    String message = "Immutable test";

    // Act
    ClsClinicEvent event = new ClsClinicEvent(type, message);

    // Assert
    assertEquals(type, event.getType());
    assertEquals(message, event.getMessage());
    assertEquals(event.getType(), event.getType());
    assertEquals(event.getMessage(), event.getMessage());
  }
}
