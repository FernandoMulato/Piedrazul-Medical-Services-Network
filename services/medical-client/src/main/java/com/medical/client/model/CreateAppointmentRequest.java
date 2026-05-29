package com.medical.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateAppointmentRequest {
    private String patientDocument;
    private String patientName;
    private String patientPhone;
    private Long professionalId;
    private String professionalName;
    private String date;
    private String time;
    private Integer durationMinutes;
    private String reason;

    public CreateAppointmentRequest() {}

    // Getters
    public String getPatientDocument() { return patientDocument; }
    public String getPatientName() { return patientName; }
    public String getPatientPhone() { return patientPhone; }
    public Long getProfessionalId() { return professionalId; }
    public String getProfessionalName() { return professionalName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public String getReason() { return reason; }

    // Setters
    public void setPatientDocument(String patientDocument) { this.patientDocument = patientDocument; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setPatientPhone(String patientPhone) { this.patientPhone = patientPhone; }
    public void setProfessionalId(Long professionalId) { this.professionalId = professionalId; }
    public void setProfessionalName(String professionalName) { this.professionalName = professionalName; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public void setReason(String reason) { this.reason = reason; }
}
