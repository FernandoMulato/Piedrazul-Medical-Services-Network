package com.medical.client.model;

/**
 * Possible states for an appointment's lifecycle.
 * Mirror of com.medical.entity.AppointmentStatus from appointments-service.
 */
public enum AppointmentStatus {
    SCHEDULED,
    COMPLETED,
    CANCELLED
}
