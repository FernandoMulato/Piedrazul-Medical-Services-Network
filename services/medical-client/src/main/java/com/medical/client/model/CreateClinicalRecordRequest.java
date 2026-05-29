package com.medical.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

/**
 * Request DTO for creating a new clinical record.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateClinicalRecordRequest {

    private String patientDocument;
    private Long professionalId;
    private String professionalType;
    private String diagnosis;
    private String treatment;
    private String observations;
    private LocalDate consultationDate;

    public CreateClinicalRecordRequest() {
    }

    // Getters
    public String getPatientDocument() { return patientDocument; }
    public Long getProfessionalId() { return professionalId; }
    public String getProfessionalType() { return professionalType; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatment() { return treatment; }
    public String getObservations() { return observations; }
    public LocalDate getConsultationDate() { return consultationDate; }

    // Setters
    public void setPatientDocument(String patientDocument) { this.patientDocument = patientDocument; }
    public void setProfessionalId(Long professionalId) { this.professionalId = professionalId; }
    public void setProfessionalType(String professionalType) { this.professionalType = professionalType; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public void setObservations(String observations) { this.observations = observations; }
    public void setConsultationDate(LocalDate consultationDate) { this.consultationDate = consultationDate; }
}
