package com.piedrazul.Domain.core.events;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link ClsClinicEventBus} (Observer pattern Subject).
 */
class UTestClsClinicEventBus {

  private ClsClinicEventBus eventBus;

  @BeforeEach
  void setUp() {
    eventBus = new ClsClinicEventBus();
  }

  @Test
  @DisplayName("Subscribe and publish - single observer receives event (normal case)")
  void subscribeAndPublishSingleObserverReceivesEvent() {
    // Arrange
    IClinicObserver observer = mock(IClinicObserver.class);
    ClsClinicEvent event = new ClsClinicEvent(ClinicEventType.USER_CHANGE, "User updated");
    eventBus.subscribe(observer);

    // Act
    eventBus.publish(event);

    // Assert
    verify(observer).onEvent(eq(event));
  }

  @Test
  @DisplayName("Subscribe multiple observers - all receive published event (normal case)")
  void subscribeMultipleObserversAllReceiveEvent() {
    // Arrange
    IClinicObserver observer1 = mock(IClinicObserver.class);
    IClinicObserver observer2 = mock(IClinicObserver.class);
    ClsClinicEvent event = new ClsClinicEvent(ClinicEventType.STATUS_MESSAGE, "Status update");
    eventBus.subscribe(observer1);
    eventBus.subscribe(observer2);

    // Act
    eventBus.publish(event);

    // Assert
    verify(observer1).onEvent(eq(event));
    verify(observer2).onEvent(eq(event));
  }

  @Test
  @DisplayName("Unsubscribe - observer no longer receives events (normal case)")
  void unsubscribeObserverNoLongerReceivesEvents() {
    // Arrange
    IClinicObserver observer = mock(IClinicObserver.class);
    ClsClinicEvent event = new ClsClinicEvent(ClinicEventType.USER_CHANGE, "User changed");
    eventBus.subscribe(observer);
    eventBus.unsubscribe(observer);

    // Act
    eventBus.publish(event);

    // Assert
    verify(observer, never()).onEvent(any());
  }

  @Test
  @DisplayName("Subscribe null observer - does not add and publish does not fail (borderline case)")
  void subscribeNullObserverDoesNotAdd() {
    // Arrange
    IClinicObserver observer = mock(IClinicObserver.class);
    ClsClinicEvent event = new ClsClinicEvent(ClinicEventType.STATUS_MESSAGE, "Test");
    eventBus.subscribe(null);
    eventBus.subscribe(observer);

    // Act
    eventBus.publish(event);

    // Assert - only the valid observer receives the event
    verify(observer).onEvent(eq(event));
  }

  @Test
  @DisplayName("Publish with zero observers - does not throw (borderline case)")
  void publishWithZeroObserversDoesNotThrow() {
    // Arrange
    ClsClinicEvent event = new ClsClinicEvent(ClinicEventType.USER_CHANGE, "No observers");

    // Act & Assert
    assertDoesNotThrow(() -> eventBus.publish(event));
  }

  @Test
  @DisplayName("Unsubscribe observer that was never subscribed - does not throw (borderline case)")
  void unsubscribeNonSubscribedObserverDoesNotThrow() {
    // Arrange
    IClinicObserver observer = mock(IClinicObserver.class);

    // Act & Assert
    assertDoesNotThrow(() -> eventBus.unsubscribe(observer));
  }

  @Test
  @DisplayName("Subscribe same observer twice - receives event twice (borderline case)")
  void subscribeSameObserverTwiceReceivesEventTwice() {
    // Arrange
    IClinicObserver observer = mock(IClinicObserver.class);
    ClsClinicEvent event = new ClsClinicEvent(ClinicEventType.USER_CHANGE, "Duplicate");
    eventBus.subscribe(observer);
    eventBus.subscribe(observer);

    // Act
    eventBus.publish(event);

    // Assert
    verify(observer, times(2)).onEvent(eq(event));
  }

  @Test
  @DisplayName("Observer throwing exception propagates and stops notification to remaining observers (error case)")
  void observerThrowingExceptionPropagates() {
    // Arrange
    IClinicObserver failingObserver = mock(IClinicObserver.class);
    IClinicObserver succeedingObserver = mock(IClinicObserver.class);
    ClsClinicEvent event = new ClsClinicEvent(ClinicEventType.STATUS_MESSAGE, "Error test");
    eventBus.subscribe(failingObserver);
    eventBus.subscribe(succeedingObserver);

    org.mockito.Mockito.doThrow(new RuntimeException("Observer error")).when(failingObserver).onEvent(any());

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> eventBus.publish(event));
    assertEquals("Observer error", thrown.getMessage());

    verify(failingObserver).onEvent(eq(event));
    verify(succeedingObserver, never()).onEvent(any());
  }

  @Test
  @DisplayName("Publish null event passes null to observer (error/borderline case)")
  void publishNullEventPassesNullToObserver() {
    // Arrange
    IClinicObserver observer = mock(IClinicObserver.class);
    eventBus.subscribe(observer);

    // Act
    eventBus.publish(null);

    // Assert
    verify(observer).onEvent(eq(null));
  }

  @Test
  @DisplayName("IClinicObserver implementation receives event via onEvent (normal case)")
  void clinicObserverImplementationReceivesEvent() {
    // Arrange - concrete implementation capturing events
    List<ClsClinicEvent> receivedEvents = new ArrayList<>();
    IClinicObserver observer = event -> receivedEvents.add(event);
    ClsClinicEvent event = new ClsClinicEvent(ClinicEventType.USER_CHANGE, "User logged in");
    eventBus.subscribe(observer);

    // Act
    eventBus.publish(event);

    // Assert
    assertEquals(1, receivedEvents.size());
    assertEquals(event, receivedEvents.get(0));
    assertEquals(ClinicEventType.USER_CHANGE, receivedEvents.get(0).getType());
    assertEquals("User logged in", receivedEvents.get(0).getMessage());
  }
}
