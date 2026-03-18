package com.piedrazul.Presentation.controller;

import java.sql.Date;

import javax.swing.JDialog;

import com.piedrazul.Presentation.views.AppointmentView;

public class AppointmentController {
    private AppointmentView appointmentView;

    public AppointmentController (AppointmentView view) {  
        this.appointmentView = view;
        view.addAgendarListener(e -> fncAgendar());
        view.addLimpiarListener(e -> fncLimpiar());
    }

    private void fncAgendar () {
        try {
            long patientID = Long.parseLong(appointmentView.getPatientID());
            String phoneNumber = appointmentView.getPhoneNumber();
            long medID = Long.parseLong(appointmentView.getMedID());
            java.util.Date dateTime = appointmentView.getDate();

            //  appointmentView.showDialog(dateTime.toString()); // debug fecha y hora

            appointmentView.showDialog("Agendando cita...");

        } catch (Exception e) {
            appointmentView.showDialog("Ha ocurrido un error, por favor verifique los datos diligenciados e intente nuevamente.");
        }
    }
    
    private void fncLimpiar () {
        appointmentView.setPatientID(null);
        appointmentView.setPhoneNumber(null);
        appointmentView.setMedID(null);
        appointmentView.setDate(null);
        appointmentView.setSpeciality(null);
    }
}
