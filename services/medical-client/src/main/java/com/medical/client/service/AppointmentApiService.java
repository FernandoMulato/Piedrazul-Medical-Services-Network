package com.medical.client.service;

import com.medical.client.config.ApiConfig;
import com.medical.client.model.AppointmentResponse;
import com.medical.client.model.CreateAppointmentRequest;
import com.medical.client.model.UpdateAppointmentRequest;

import java.util.List;

public class AppointmentApiService {

    private final HttpClientService httpClient = HttpClientService.getInstance();

    public List<AppointmentResponse> getAllAppointments() throws Exception {
        return httpClient.getList(ApiConfig.APPOINTMENTS_BASE_URL, AppointmentResponse.class);
    }

    public List<AppointmentResponse> getAppointmentsByPatient(String documentNumber) throws Exception {
        return httpClient.getList(ApiConfig.APPOINTMENTS_BASE_URL + "/patient/" + documentNumber, AppointmentResponse.class);
    }

    public AppointmentResponse createAppointment(CreateAppointmentRequest request) throws Exception {
        return httpClient.post(ApiConfig.APPOINTMENTS_BASE_URL, request, AppointmentResponse.class);
    }

    public AppointmentResponse updateAppointment(Long id, UpdateAppointmentRequest request) throws Exception {
        return httpClient.put(ApiConfig.APPOINTMENTS_BASE_URL + "/" + id, request, AppointmentResponse.class);
    }

    public void cancelAppointment(Long id) throws Exception {
        httpClient.patch(ApiConfig.APPOINTMENTS_BASE_URL + "/" + id + "/cancel");
    }
}
