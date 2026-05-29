package com.piedrazul.clinicalrecords.strategy;

import com.piedrazul.clinicalrecords.dto.CreateClinicalRecordRequest;
import org.springframework.stereotype.Component;

@Component
public class DentistClinicalRecordStrategy
        implements ClinicalRecordStrategy {

    @Override
    public void validate(
            CreateClinicalRecordRequest request) {

        if(request.getDiagnosis().length() < 3) {

            throw new RuntimeException(
                    "Invalid diagnosis");
        }
    }
}