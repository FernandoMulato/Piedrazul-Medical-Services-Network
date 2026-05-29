package com.medical.client.service;

import com.medical.client.config.ApiConfig;
import com.medical.client.model.ClinicalRecordResponse;
import com.medical.client.model.CreateClinicalRecordRequest;
import com.medical.client.model.UpdateClinicalRecordRequest;

import java.util.List;

public class ClinicalRecordApiService {

    private final HttpClientService httpClient = HttpClientService.getInstance();

    public List<ClinicalRecordResponse> getByPatient(String patientDocument) throws Exception {
        return httpClient.getList(ApiConfig.CLINICAL_RECORDS_BASE_URL + "/patient/" + patientDocument, ClinicalRecordResponse.class);
    }

    public ClinicalRecordResponse getById(Long id) throws Exception {
        return httpClient.get(ApiConfig.CLINICAL_RECORDS_BASE_URL + "/" + id, ClinicalRecordResponse.class);
    }

    public ClinicalRecordResponse create(CreateClinicalRecordRequest request) throws Exception {
        return httpClient.post(ApiConfig.CLINICAL_RECORDS_BASE_URL, request, ClinicalRecordResponse.class);
    }

    public ClinicalRecordResponse update(Long id, UpdateClinicalRecordRequest request) throws Exception {
        return httpClient.put(ApiConfig.CLINICAL_RECORDS_BASE_URL + "/" + id, request, ClinicalRecordResponse.class);
    }

    public void delete(Long id) throws Exception {
        httpClient.delete(ApiConfig.CLINICAL_RECORDS_BASE_URL + "/" + id);
    }
}
