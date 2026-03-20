package com.piedrazul.Domain.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Observer pattern: Subject / publisher of events. Views subscribe,
 * model/service publishes.
 */
public class ClsClinicEventBus {
  private final List<IClinicObserver> observers = new CopyOnWriteArrayList<>();

  public void subscribe(IClinicObserver observer) {
    if (observer != null)
      observers.add(observer);
  }

  public void unsubscribe(IClinicObserver observer) {
    observers.remove(observer);
  }

  public void publish(ClsClinicEvent event) {
    for (var o : observers) {
      o.onEvent(event);
    }
  }
}
