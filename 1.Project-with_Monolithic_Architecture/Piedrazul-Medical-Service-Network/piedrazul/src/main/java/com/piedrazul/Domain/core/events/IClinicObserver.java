package com.piedrazul.Domain.core.events;

/**
 * Observer interface for receiving clinic events.
 */
public interface IClinicObserver {
  void onEvent(ClsClinicEvent event);
}
