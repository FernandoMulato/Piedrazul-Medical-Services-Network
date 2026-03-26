package com.piedrazul.Domain.core.events;

/**
 * Represents an event within the clinic system.
 * <p>
 * This class encapsulates the information that is sent through the
 * event bus to notify observers about changes or actions occurring
 * in the system.
 * </p>
 *
 * <p>
 * Each event contains a {@link ClinicEventType} that defines the
 * nature of the event, and an optional message providing additional
 * context.
 * </p>
 * 
 * @author Henry Fernando Mulato Llanten
 */
public class ClsClinicEvent {

  /**
   * Type of the event, defining its category or purpose.
   */
  private final ClinicEventType type;

  /**
   * Descriptive message associated with the event.
   */
  private final String message;

  /**
   * Constructs a new clinic event with the specified type and message.
   *
   * @param type    the type of the event
   * @param message additional information related to the event
   */
  public ClsClinicEvent(ClinicEventType type, String message) {
    this.type = type;
    this.message = message;
  }

  /**
   * Returns the type of the event.
   *
   * @return event type
   */
  public ClinicEventType getType() {
    return type;
  }

  /**
   * Returns the descriptive message of the event.
   *
   * @return event message
   */
  public String getMessage() {
    return message;
  }
}
