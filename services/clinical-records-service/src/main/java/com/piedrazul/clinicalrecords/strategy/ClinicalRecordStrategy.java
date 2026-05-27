package com.piedrazul.clinicalrecords.strategy;

import com.piedrazul.clinicalrecords.dto.CreateClinicalRecordRequest;

public interface ClinicalRecordStrategy {

    void validate(CreateClinicalRecordRequest request);
}