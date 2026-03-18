package com.piedrazul.Presentation.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import com.piedrazul.Domain.entities.ClsAppointment;
import com.piedrazul.Domain.enums.AppointmentStatus;
import com.piedrazul.Domain.enums.AttentionType;
import com.piedrazul.Infrastructure.repository.IAppointmentRepository;
import com.piedrazul.Presentation.views.AppointmentView;

public class AppointmentController {
    private AppointmentView appointmentView;
    private IAppointmentRepository appointmentRepository;

    public AppointmentController(AppointmentView view, IAppointmentRepository repository) {  
        this.appointmentView = view;
        this.appointmentRepository = repository;
        view.addAgendarListener(e -> fncAgendar());
        view.addReagendarListener(e -> fncReagendar());
        view.addExportarListener(e -> fncExportar());
        view.addLimpiarListener(e -> fncLimpiar());
    }

    private void fncAgendar() {
        try {
            if (appointmentView.getPatientID().trim().isEmpty()
                || appointmentView.getPhoneNumber().trim().isEmpty()
                || appointmentView.getMedID().trim().isEmpty()
                || appointmentView.getReason().trim().isEmpty()) {
                appointmentView.showDialog("Error: Todos los campos son obligatorios.");
                return;
            }

            long patientID = Long.parseLong(appointmentView.getPatientID());
            String phoneNumber = appointmentView.getPhoneNumber();
            long medID = Long.parseLong(appointmentView.getMedID());
            java.util.Date dateTime = appointmentView.getDate();
            String especialidad = appointmentView.getEspecialidad();
            String reason = appointmentView.getReason();

            AttentionType attType = "Neuroterapia".equals(especialidad)
                ? AttentionType.Specialized
                : AttentionType.General;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateTimeStr = sdf.format(dateTime);

            if (appointmentRepository.opCheckScheduleConflict(medID, dateTimeStr)) {
                appointmentView.showDialog("Error: El profesional ya tiene una cita programada en ese horario.");
                return;
            }

            Date sqlDate = new Date(dateTime.getTime());

            ClsAppointment appointment = new ClsAppointment(
                patientID, phoneNumber, medID, sqlDate, attType, reason
            );

            ClsAppointment created = appointmentRepository.opCreate(appointment);

            if (created != null && created.getAttId() > 0) {
                appointmentView.showDialog("¡Cita agendada exitosamente! ID: " + created.getAttId());
                fncLimpiar();
            } else {
                appointmentView.showDialog("Error: No se pudo guardar la cita.");
            }

        } catch (NumberFormatException e) {
            appointmentView.showDialog("Error: Los campos de ID deben ser numéricos.");
        } catch (Exception e) {
            appointmentView.showDialog("Ha ocurrido un error, por favor verifique los datos diligenciados e intente nuevamente.");
        }
    }

    private void fncReagendar() {
        try {
            String appointmentIdStr = appointmentView.getAppointmentId();
            if (appointmentIdStr == null || appointmentIdStr.trim().isEmpty()) {
                appointmentView.showDialog("Error: Ingrese el ID de la cita a reagendar.");
                return;
            }

            long appointmentId = Long.parseLong(appointmentIdStr);

            ClsAppointment existing = appointmentRepository.opView(appointmentId);
            if (existing == null) {
                appointmentView.showDialog("Error: No se encontró la cita con ID " + appointmentId + ".");
                return;
            }

            java.util.Date newDateTime = appointmentView.getDate();
            String medIdStr = appointmentView.getMedID();
            long newMedId = medIdStr.trim().isEmpty() ? existing.getAttMedicalStaffId() : Long.parseLong(medIdStr);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String newDateTimeStr = sdf.format(newDateTime);

            if (appointmentRepository.opCheckScheduleConflict(newMedId, newDateTimeStr)) {
                appointmentView.showDialog("Error: El profesional ya tiene una cita programada en ese horario.");
                return;
            }

            existing.setAttDateAndTime(new Date(newDateTime.getTime()));
            existing.setAttMedicalStaffId(newMedId);
            existing.setAttStatus(AppointmentStatus.RESCHEDULED);

            String newReason = appointmentView.getReason();
            if (newReason != null && !newReason.trim().isEmpty()) {
                existing.setAttReason(newReason);
            }

            boolean updated = appointmentRepository.opUpdate(existing);

            if (updated) {
                appointmentView.showDialog("¡Cita reagendada exitosamente! Estado: REAGENDADA");
                fncLimpiar();
            } else {
                appointmentView.showDialog("Error: No se pudo reagendar la cita.");
            }

        } catch (NumberFormatException e) {
            appointmentView.showDialog("Error: Los campos de ID deben ser numéricos.");
        } catch (Exception e) {
            appointmentView.showDialog("Error al reagendar: " + e.getMessage());
        }
    }

    private void fncExportar() {
        try {
            List<ClsAppointment> appointments = appointmentRepository.opListAll();

            if (appointments.isEmpty()) {
                appointmentView.showDialog("No hay citas registradas para exportar.");
                return;
            }

            String fileName = "citas_exportadas.csv";

            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write("ID,Documento_Paciente,Telefono,ID_Profesional,FechaHora,TipoAtencion,Estado,Motivo\n");

                for (ClsAppointment app : appointments) {
                    writer.write(String.format("%d,%d,%s,%d,%s,%s,%s,%s\n",
                        app.getAttId(),
                        app.getAttCitizenshipCardPatient(),
                        app.getAttPhoneNumber(),
                        app.getAttMedicalStaffId(),
                        app.getAttDateAndTime(),
                        app.getAttAttentionType(),
                        app.getAttStatus(),
                        app.getAttReason().replace(",", ";")
                    ));
                }
            }

            appointmentView.showDialog("¡Citas exportadas exitosamente a '" + fileName + "'!\nTotal: " + appointments.size() + " citas.");

        } catch (IOException e) {
            appointmentView.showDialog("Error al exportar: " + e.getMessage());
        } catch (Exception e) {
            appointmentView.showDialog("Error inesperado al exportar: " + e.getMessage());
        }
    }
    
    private void fncLimpiar() {
        appointmentView.setAppointmentId(null);
        appointmentView.setPatientID(null);
        appointmentView.setPhoneNumber(null);
        appointmentView.setMedID(null);
        appointmentView.setDate(new java.util.Date());
        appointmentView.setSpeciality(null);
        appointmentView.setReason(null);
    }
}
