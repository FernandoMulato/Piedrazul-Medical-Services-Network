package com.piedrazul.clinicalrecords.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateClinicalRecordRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Professional ID is required")
    private Long professionalId;

    @NotBlank(message = "Professional type is required")
    private String professionalType;

    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;

    @NotBlank(message = "Treatment is required")
    private String treatment;

    private String observations;

    @NotNull(message = "Consultation date is required")
    private LocalDate consultationDate;
}