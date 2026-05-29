package com.medical.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for clinical record data.
 * Mirror of com.piedrazul.clinicalrecords.entity.ClinicalRecord.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClinicalRecordResponse {

    private Long id;
    private Long patientId;
    private Long professionalId;
    private String professionalType;
    private String diagnosis;
    private String treatment;
    private String observations;
    private LocalDate consultationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ClinicalRecordResponse() {
    }

    // Getters
    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public Long getProfessionalId() { return professionalId; }
    public String getProfessionalType() { return professionalType; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatment() { return treatment; }
    public String getObservations() { return observations; }
    public LocalDate getConsultationDate() { return consultationDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setProfessionalId(Long professionalId) { this.professionalId = professionalId; }
    public void setProfessionalType(String professionalType) { this.professionalType = professionalType; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public void setObservations(String observations) { this.observations = observations; }
    public void setConsultationDate(LocalDate consultationDate) { this.consultationDate = consultationDate; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
