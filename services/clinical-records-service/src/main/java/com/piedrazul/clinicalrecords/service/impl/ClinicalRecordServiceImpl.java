package com.piedrazul.clinicalrecords.service.impl;

import com.piedrazul.clinicalrecords.builder.ClinicalRecordBuilder;
import com.piedrazul.clinicalrecords.dto.CreateClinicalRecordRequest;
import com.piedrazul.clinicalrecords.dto.UpdateClinicalRecordRequest;
import com.piedrazul.clinicalrecords.entity.ClinicalRecord;
import com.piedrazul.clinicalrecords.exception.ResourceNotFoundException;
import com.piedrazul.clinicalrecords.repository.ClinicalRecordRepository;
import com.piedrazul.clinicalrecords.service.ClinicalRecordService;
import com.piedrazul.clinicalrecords.strategy.ClinicalRecordStrategy;
import com.piedrazul.clinicalrecords.strategy.ClinicalRecordStrategyFactory;
import com.piedrazul.clinicalrecords.observer.ClinicalRecordObserverManager;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicalRecordServiceImpl
        implements ClinicalRecordService {

    
    private final ClinicalRecordRepository repository;

    private final ClinicalRecordStrategyFactory strategyFactory;
    private final ClinicalRecordObserverManager observerManager;

    @Override
    public ClinicalRecord create(
            CreateClinicalRecordRequest request) {

        ClinicalRecordStrategy strategy =
                strategyFactory.getStrategy(
                        request.getProfessionalType());

        strategy.validate(request);

        ClinicalRecord record =
                ClinicalRecordBuilder.fromRequest(request);

        ClinicalRecord savedRecord =
        repository.save(record);

        observerManager.notifyObservers(savedRecord);

        return savedRecord;
    }

    @Override
    public ClinicalRecord findById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                        "Clinical record not found"));
    }

    @Override
    public List<ClinicalRecord> findByPatient(Long patientId) {

        return repository.findByPatientId(patientId);
    }

    @Override
    public ClinicalRecord update(
            Long id,
            UpdateClinicalRecordRequest request) {

        ClinicalRecord record =
                repository.findById(id)
                        .orElseThrow(() ->
                                 new ResourceNotFoundException(
                        "Clinical record not found"));

        record.setDiagnosis(request.getDiagnosis());
        record.setTreatment(request.getTreatment());
        record.setObservations(request.getObservations());
        record.setUpdatedAt(LocalDateTime.now());

        return repository.save(record);
    }

         @Override
        public void delete(Long id) {

        ClinicalRecord record =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Clinical record not found"));

        repository.delete(record);
        }
}