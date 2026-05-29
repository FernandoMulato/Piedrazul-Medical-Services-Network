package com.piedrazul.clinicalrecords.strategy;

import com.piedrazul.clinicalrecords.dto.CreateClinicalRecordRequest;

import org.springframework.stereotype.Component;

@Component
public class PhysicianClinicalRecordStrategy
        implements ClinicalRecordStrategy {

    @Override
    public void validate(
            CreateClinicalRecordRequest request) {

        if (request.getDiagnosis() == null
                || request.getDiagnosis().isBlank()) {

            throw new RuntimeException(
                    "Diagnosis is required for physicians");
        }
    }
}