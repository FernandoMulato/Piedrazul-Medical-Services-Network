package com.medical.client.event;

public class ClinicEvent {
    private final ClinicEventType eventType;
    private final Object data;

    public ClinicEvent(ClinicEventType eventType, Object data) {
        this.eventType = eventType;
        this.data = data;
    }

    public ClinicEventType getEventType() {
        return eventType;
    }

    public Object getData() {
        return data;
    }
}
