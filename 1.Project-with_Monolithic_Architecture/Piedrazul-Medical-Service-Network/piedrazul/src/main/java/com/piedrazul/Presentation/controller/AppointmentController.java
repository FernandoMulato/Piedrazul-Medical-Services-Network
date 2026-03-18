package com.piedrazul.Presentation.controller;

import com.piedrazul.Presentation.views.AppointmentView;

public class AppointmentController {
    AppointmentView appointmentView;

    public AppointmentController (AppointmentView view) {
        view.addAgendarListener(e -> fncAgendar());
        view.addLimpiarListener(e -> fncLimpiar());
    }

    private void fncAgendar () {
        String patientID = appointmentView.getPatientID();
    }
    
    private void fncLimpiar () {
        System.out.println("Limpiar");
    }
}
