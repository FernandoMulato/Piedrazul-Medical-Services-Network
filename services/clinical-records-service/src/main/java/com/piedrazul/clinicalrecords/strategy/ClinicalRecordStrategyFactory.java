package com.piedrazul.clinicalrecords.strategy;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClinicalRecordStrategyFactory {

    private final PhysicianClinicalRecordStrategy physicianStrategy;

    private final TherapistClinicalRecordStrategy therapistStrategy;

    public ClinicalRecordStrategy getStrategy(
            String professionalType) {

        return switch (professionalType.toUpperCase()) {

            case "PHYSICIAN" -> physicianStrategy;

            case "THERAPIST" -> therapistStrategy;

            default -> throw new RuntimeException(
                    "Invalid professional type");
        };
    }
}