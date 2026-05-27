package com.piedrazul.clinicalrecords.dto;

import lombok.Data;

@Data
public class UpdateClinicalRecordRequest {

    private String diagnosis;

    private String treatment;

    private String observations;
}