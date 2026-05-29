package com.piedrazul.clinicalrecords.service;

import com.piedrazul.clinicalrecords.dto.CreateClinicalRecordRequest;
import com.piedrazul.clinicalrecords.dto.UpdateClinicalRecordRequest;
import com.piedrazul.clinicalrecords.entity.ClinicalRecord;


import java.util.List;

public interface ClinicalRecordService {
    ClinicalRecord findById(Long id);

    ClinicalRecord create(CreateClinicalRecordRequest request);

    List<ClinicalRecord> findByPatient(Long patientId);

    ClinicalRecord update(Long id,
                          UpdateClinicalRecordRequest request);
    void delete(Long id);
}