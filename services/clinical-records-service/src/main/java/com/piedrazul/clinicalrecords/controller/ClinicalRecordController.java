package com.piedrazul.clinicalrecords.controller;

import com.piedrazul.clinicalrecords.dto.CreateClinicalRecordRequest;
import com.piedrazul.clinicalrecords.dto.UpdateClinicalRecordRequest;
import com.piedrazul.clinicalrecords.entity.ClinicalRecord;
import com.piedrazul.clinicalrecords.facade.ClinicalRecordFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for clinical records management.
 * Routes: /api/clinical-records/**
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@RestController
@RequestMapping("/api/clinical-records")
@RequiredArgsConstructor
public class ClinicalRecordController {

    private final ClinicalRecordFacade facade;

    @PostMapping
    public ClinicalRecord create(
            @Valid
            @RequestBody CreateClinicalRecordRequest request) {

        return facade.register(request);
    }
    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id) {

        facade.delete(id);

        return "Clinical record deleted successfully";
    }

    @GetMapping("/patient/{patientId}")
    public List<ClinicalRecord> findByPatient(
            @PathVariable Long patientId) {

        return facade.consult(patientId);
    }

    @PutMapping("/{id}")
    public ClinicalRecord update(
            @PathVariable Long id,
            @RequestBody UpdateClinicalRecordRequest request) {

        return facade.update(id, request);
    }

    @GetMapping("/{id}")
    public ClinicalRecord findById(
            @PathVariable Long id) {

        return facade.findOne(id);
    }
}