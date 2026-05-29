package com.piedrazul.clinicalrecords.facade;

import com.piedrazul.clinicalrecords.client.UserClient; // Importamos el cliente nuevo
import com.piedrazul.clinicalrecords.dto.CreateClinicalRecordRequest;
import com.piedrazul.clinicalrecords.dto.UpdateClinicalRecordRequest;
import com.piedrazul.clinicalrecords.entity.ClinicalRecord;
import com.piedrazul.clinicalrecords.service.ClinicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClinicalRecordFacade {

    private final ClinicalRecordService service;
    private final UserClient userClient; // Inyectamos la interfaz

    public ClinicalRecord register(CreateClinicalRecordRequest request) {
        
        //El "guardaespaldas": Valida si el ID del paciente existe en el microservicio de externos
        //userClient.validateUserExists(request.getPatientId());

        // Tu código original intacto:
        return service.create(request);
    }

    public List<ClinicalRecord> consult(Long patientId) {
        return service.findByPatient(patientId);
    }

    public ClinicalRecord update(Long id, UpdateClinicalRecordRequest request) {
        return service.update(id, request);
    }

    public ClinicalRecord findOne(Long id) {
        return service.findById(id);
    }

    public void delete(Long id) {
        service.delete(id);
    }
}