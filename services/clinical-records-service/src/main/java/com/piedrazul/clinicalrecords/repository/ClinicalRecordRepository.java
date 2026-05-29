package com.piedrazul.clinicalrecords.repository;

import com.piedrazul.clinicalrecords.entity.ClinicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicalRecordRepository
        extends JpaRepository<ClinicalRecord, Long> {

    List<ClinicalRecord> findByPatientId(Long patientId);
}