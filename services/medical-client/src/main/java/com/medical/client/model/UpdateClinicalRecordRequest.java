package com.medical.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request DTO for updating a clinical record.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateClinicalRecordRequest {

    private String diagnosis;
    private String treatment;
    private String observations;

    public UpdateClinicalRecordRequest() {
    }

    public UpdateClinicalRecordRequest(String diagnosis, String treatment, String observations) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.observations = observations;
    }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
}
