package com.piedrazul.clinicalrecords.strategy;

import com.piedrazul.clinicalrecords.dto.CreateClinicalRecordRequest;

import org.springframework.stereotype.Component;

@Component
public class TherapistClinicalRecordStrategy
        implements ClinicalRecordStrategy {

    @Override
    public void validate(
            CreateClinicalRecordRequest request) {

        if (request.getObservations() == null
                || request.getObservations().isBlank()) {

            throw new RuntimeException(
                    "Observations are required for therapists");
        }
    }
}