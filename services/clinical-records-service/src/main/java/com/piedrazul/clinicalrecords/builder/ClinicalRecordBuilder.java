package com.piedrazul.clinicalrecords.builder;

import com.piedrazul.clinicalrecords.dto.CreateClinicalRecordRequest;
import com.piedrazul.clinicalrecords.entity.ClinicalRecord;

import java.time.LocalDateTime;

public class ClinicalRecordBuilder {

    private final ClinicalRecord record;

    public ClinicalRecordBuilder() {

        this.record = new ClinicalRecord();
    }

    public ClinicalRecordBuilder patientId(Long patientId) {

        record.setPatientId(patientId);
        return this;
    }

    public ClinicalRecordBuilder professionalId(
            Long professionalId) {

        record.setProfessionalId(professionalId);
        return this;
    }

    public ClinicalRecordBuilder professionalType(
            String professionalType) {

        record.setProfessionalType(professionalType);
        return this;
    }

    public ClinicalRecordBuilder diagnosis(
            String diagnosis) {

        record.setDiagnosis(diagnosis);
        return this;
    }

    public ClinicalRecordBuilder treatment(
            String treatment) {

        record.setTreatment(treatment);
        return this;
    }

    public ClinicalRecordBuilder observations(
            String observations) {

        record.setObservations(observations);
        return this;
    }

    public ClinicalRecordBuilder consultationDate(
            java.time.LocalDate consultationDate) {

        record.setConsultationDate(consultationDate);
        return this;
    }

    public ClinicalRecord build() {

        record.setCreatedAt(LocalDateTime.now());

        return record;
    }

    public static ClinicalRecord fromRequest(
            CreateClinicalRecordRequest request) {

        return new ClinicalRecordBuilder()
                .patientId(request.getPatientId())
                .professionalId(request.getProfessionalId())
                .professionalType(request.getProfessionalType())
                .diagnosis(request.getDiagnosis())
                .treatment(request.getTreatment())
                .observations(request.getObservations())
                .consultationDate(request.getConsultationDate())
                .build();
    }
}