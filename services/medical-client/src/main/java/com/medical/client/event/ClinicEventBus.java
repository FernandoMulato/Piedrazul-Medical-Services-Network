package com.medical.client.event;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class ClinicEventBus {
    private static ClinicEventBus instance;
    private final List<ClinicObserver> observers = new ArrayList<>();

    private ClinicEventBus() {}

    public static ClinicEventBus getInstance() {
        if (instance == null) {
            instance = new ClinicEventBus();
        }
        return instance;
    }

    public void subscribe(ClinicObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unsubscribe(ClinicObserver observer) {
        observers.remove(observer);
    }

    public void publish(ClinicEvent event) {
        // Ensure UI updates happen on the JavaFX application thread
        Platform.runLater(() -> {
            for (ClinicObserver observer : observers) {
                observer.onEvent(event);
            }
        });
    }
}
