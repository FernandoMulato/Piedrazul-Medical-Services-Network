package com.piedrazul.Presentation.controller;

import java.awt.Component;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.piedrazul.Application.services.IAppointmentService;
import com.piedrazul.Application.services.impl.ClsAppointmentServiceImpl;
import com.piedrazul.Domain.entities.ClsAppointment;
import com.piedrazul.Domain.enums.AttentionType;
import com.piedrazul.Presentation.views.AppointmentView;

public class AppointmentController {

    private final AppointmentView appointmentView;
    private final IAppointmentService appointmentService;

    public AppointmentController(AppointmentView view, IAppointmentService service) {
        this.appointmentView = view;
        this.appointmentService = service;
        view.addScheduleListener(e -> opSchedule());
        view.addReScheduleListener(e -> opReSchedule());
        view.addExportListener(e -> opExport());
        view.addClearScheduleListener(e -> opClearSchedule());
        view.addClearReScheduleListener(e -> opClearReSchedule());
        view.addSearchAppointmentListener(e -> opSearchAppointment());
    }

    private void opSchedule() {
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
            java.util.Date dateTime = appointmentView.getScheduleDate();
            String especialidad = appointmentView.getEspecialidad();
            String reason = appointmentView.getReason();

            AttentionType attType = ClsAppointmentServiceImpl.resolveAttentionType(especialidad);

            ClsAppointment appointment = new ClsAppointment(
                    patientID, phoneNumber, medID, dateTime, attType, reason);

            ClsAppointment created = appointmentService.opSchedule(appointment);

            appointmentView.showDialog("¡Cita agendada exitosamente! ID: " + created.getAttId());
            opClearSchedule();

        } catch (NumberFormatException e) {
            appointmentView.showDialog("Error: Los campos de ID deben ser numericos.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            appointmentView.showDialog("Error: " + e.getMessage());
        } catch (Exception e) {
            appointmentView.showDialog("Ha ocurrido un error: " + e.getMessage());
        }
    }

    private void opReSchedule() {
        try {
            String appointmentIdStr = appointmentView.getAppointmentId();
            if (appointmentIdStr == null || appointmentIdStr.trim().isEmpty()) {
                appointmentView.showDialog("Error: Ingrese el ID de la cita a reagendar.");
                return;
            }

            long appointmentId = Long.parseLong(appointmentIdStr);
            java.util.Date newDateTime = appointmentView.getReScheduleDate();
            String medIdStr = appointmentView.getMedID();
            long newMedId = medIdStr.trim().isEmpty() ? 0 : Long.parseLong(medIdStr);
            String newReason = appointmentView.getReason();

            ClsAppointment patch = new ClsAppointment(0, null, newMedId, newDateTime, null, newReason);
            patch.setAttId(appointmentId);

            boolean updated = appointmentService.opReSchedule(patch);

            if (updated) {
                appointmentView.showDialog("¡Cita reagendada exitosamente! Estado: REAGENDADA");
                opClearReSchedule();
            } else {
                appointmentView.showDialog("Error: No se pudo reagendar la cita.");
            }

        } catch (NumberFormatException e) {
            appointmentView.showDialog("Error: Los campos de ID deben ser numericos.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            appointmentView.showDialog("Error: " + e.getMessage());
        } catch (Exception e) {
            appointmentView.showDialog("Error al reagendar: " + e.getMessage());
        }
    }

    private void opSearchAppointment() {
        try {
            String appointmentIdStr = appointmentView.getAppointmentId();
            if (appointmentIdStr == null || appointmentIdStr.trim().isEmpty()) {
                appointmentView.showDialog("Error: Ingrese el ID de la cita a buscar.");
                return;
            }

            long appointmentId = Long.parseLong(appointmentIdStr);
            ClsAppointment appointment = appointmentService.opFindAppointment(appointmentId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            DefaultTableModel model = new DefaultTableModel(
                    new Object[]{"ID Cita", "ID Paciente", "Teléfono", "ID Medico", "Fecha Hora", "Tipo", "Estado", "Motivo"}, 0);
            model.addRow(new Object[]{
                appointment.getAttId(),
                appointment.getAttCitizenshipCardPatient(),
                appointment.getAttPhoneNumber(),
                appointment.getAttMedicalStaffId(),
                sdf.format(appointment.getAttDateAndTime()),
                appointment.getAttAttentionType().toString(),
                appointment.getAttStatus().toString(),
                appointment.getAttReason()
            });

            appointmentView.setScheduleInfoTableModel(model);
            appointmentView.setMedID(String.valueOf(appointment.getAttMedicalStaffId()));
            appointmentView.setReScheduleDate(appointment.getAttDateAndTime());
            appointmentView.setReason(appointment.getAttReason());

        } catch (NumberFormatException e) {
            appointmentView.showDialog("Error: El ID debe ser numerico.");
        } catch (IllegalArgumentException e) {
            appointmentView.showDialog("Error: " + e.getMessage());
        } catch (Exception e) {
            appointmentView.showDialog("Error inesperado: " + e.getMessage());
        }
    }

    private void opExport() {
        try {
            List<ClsAppointment> appointments = appointmentService.opListAll();

            if (appointments.isEmpty()) {
                appointmentView.showDialog("No hay citas registradas para exportar.");
                return;
            }

            String fileName = "citas_exportadas.csv";
            SimpleDateFormat exportSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write("ID,Documento_Paciente,Telefono,ID_Profesional,FechaHora,TipoAtencion,Estado,Motivo\n");

                for (ClsAppointment app : appointments) {
                    writer.write(String.format("%d,%d,%s,%d,%s,%s,%s,%s\n",
                            app.getAttId(),
                            app.getAttCitizenshipCardPatient(),
                            app.getAttPhoneNumber(),
                            app.getAttMedicalStaffId(),
                            exportSdf.format(app.getAttDateAndTime()),
                            app.getAttAttentionType(),
                            app.getAttStatus(),
                            app.getAttReason().replace(",", ";")));
                }
            }

            appointmentView.showDialog("¡Citas exportadas exitosamente a '" + fileName + "'!\nTotal: " + appointments.size() + " citas.");

        } catch (IOException e) {
            appointmentView.showDialog("Error al exportar: " + e.getMessage());
        } catch (Exception e) {
            appointmentView.showDialog("Error inesperado al exportar: " + e.getMessage());
        }
    }

    private void opClearSchedule() {
        opClearComponents(appointmentView.getSchedulePanel());
    }

    private void opClearReSchedule() {
        opClearComponents(appointmentView.getReSchedulePanel());
    }

    private void opClearComponents(Component comp) {
        opResetValues(comp);
        if (comp instanceof java.awt.Container) {
            for (Component child : ((java.awt.Container) comp).getComponents()) {
                opClearComponents(child);
            }
        }
    }

    private void opResetValues(Component comp) {
        if (comp instanceof JTextField && !(comp instanceof javax.swing.JFormattedTextField)) {
            ((JTextField) comp).setText("");
        } else if (comp instanceof JTable) {
            JTable table = (JTable) comp;
            if (table.getModel() instanceof DefaultTableModel) {
                ((DefaultTableModel) table.getModel()).setRowCount(0);
            }
            table.clearSelection();
        }
    }
}
