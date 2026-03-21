package com.piedrazul.Domain.core.events;

public class ClsClinicEvent {
  private final ClinicEventType type;
  private final String message;

  public ClsClinicEvent(ClinicEventType type, String message) {
    this.type = type;
    this.message = message;
  }

  public ClinicEventType getType() {
    return type;
  }

  public String getMessage() {
    return message;
  }
}
