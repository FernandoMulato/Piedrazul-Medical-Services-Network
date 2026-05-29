package com.piedrazul.clinicalrecords.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
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
}