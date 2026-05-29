package com.medical.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentResponse {

    private Long id;
    private String patientDocument;
    private String patientName;
    private String patientPhone;
    private Long professionalId;
    private String professionalName;
    private LocalDate date;
    private LocalTime time;
    private Integer durationMinutes;
    private String reason;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AppointmentResponse() {}

    // Getters
    public Long getId() { return id; }
    public String getPatientDocument() { return patientDocument; }
    public String getPatientName() { return patientName; }
    public String getPatientPhone() { return patientPhone; }
    public Long getProfessionalId() { return professionalId; }
    public String getProfessionalName() { return professionalName; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public String getReason() { return reason; }
    public AppointmentStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setPatientDocument(String patientDocument) { this.patientDocument = patientDocument; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setPatientPhone(String patientPhone) { this.patientPhone = patientPhone; }
    public void setProfessionalId(Long professionalId) { this.professionalId = professionalId; }
    public void setProfessionalName(String professionalName) { this.professionalName = professionalName; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setTime(LocalTime time) { this.time = time; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public void setReason(String reason) { this.reason = reason; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
