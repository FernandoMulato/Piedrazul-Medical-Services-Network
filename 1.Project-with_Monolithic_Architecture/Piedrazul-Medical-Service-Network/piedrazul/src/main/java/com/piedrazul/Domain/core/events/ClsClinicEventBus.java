package com.piedrazul.Domain.core.events;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Event bus implementation based on the Observer pattern.
 * <p>
 * This class acts as a subject (publisher) that allows observers to subscribe
 * and receive notifications when an event is published.
 * </p>
 *
 * <p>
 * It is typically used to decouple the communication between the model/service
 * layer and the presentation layer (views), enabling a reactive architecture.
 * </p>
 *
 * <p>
 * Internally, it uses a {@link CopyOnWriteArrayList} to ensure thread-safe
 * iteration when notifying observers.
 * </p>
 * 
 * @author Henry Fernando Mulato Llanten
 */
public class ClsClinicEventBus {

  /**
   * List of registered observers subscribed to receive events.
   */
  private final List<IClinicObserver> observers = new CopyOnWriteArrayList<>();

  /**
   * Subscribes an observer to the event bus.
   * <p>
   * Once subscribed, the observer will receive all published events.
   * </p>
   *
   * @param observer the observer to be registered
   */
  public void subscribe(IClinicObserver observer) {
    if (observer != null)
      observers.add(observer);
  }

  /**
   * Unsubscribes an observer from the event bus.
   * <p>
   * The observer will no longer receive event notifications.
   * </p>
   *
   * @param observer the observer to be removed
   */
  public void unsubscribe(IClinicObserver observer) {
    observers.remove(observer);
  }

  /**
   * Publishes an event to all subscribed observers.
   * <p>
   * Each observer is notified by invoking its {@code onEvent} method.
   * </p>
   *
   * @param event the event to be published
   */
  public void publish(ClsClinicEvent event) {
    for (var o : observers) {
      o.onEvent(event);
    }
  }
}
