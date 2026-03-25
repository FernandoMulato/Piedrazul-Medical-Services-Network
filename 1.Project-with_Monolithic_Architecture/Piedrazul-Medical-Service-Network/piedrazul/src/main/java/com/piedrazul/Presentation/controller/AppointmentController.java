package com.piedrazul.Presentation.controller;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

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

            AttentionType attType = "Neuroterapia".equals(especialidad)
                ? AttentionType.SPECIALIZED
                : AttentionType.GENERAL;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateTimeStr = sdf.format(dateTime);

            if (appointmentRepository.opCheckScheduleConflict(medID, dateTimeStr)) {
                appointmentView.showDialog("Error: El profesional ya tiene una cita programada en ese horario.");
                return;
            }

            ClsAppointment appointment = new ClsAppointment(
                patientID, phoneNumber, medID, dateTime, attType, reason
            );

            ClsAppointment created = appointmentRepository.opCreate(appointment);

            if (created != null && created.getAttId() > 0) {
                appointmentView.showDialog("¡Cita agendada exitosamente! ID: " + created.getAttId());
                opClearSchedule();
            } else {
                appointmentView.showDialog("Error: No se pudo guardar la cita.");
            }

        } catch (NumberFormatException e) {
            appointmentView.showDialog("Error: Los campos de ID deben ser numericos.");
        } catch (Exception e) {
            appointmentView.showDialog("Ha ocurrido un error, por favor verifique los datos diligenciados e intente nuevamente." + e.toString());
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

            ClsAppointment existing = appointmentRepository.opView(appointmentId);
            if (existing == null) {
                appointmentView.showDialog("Error: No se encontró la cita con ID " + appointmentId + ".");
                return;
            }

            java.util.Date newDateTime = appointmentView.getReScheduleDate();
            String medIdStr = appointmentView.getMedID();
            long newMedId = medIdStr.trim().isEmpty() ? existing.getAttMedicalStaffId() : Long.parseLong(medIdStr);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String newDateTimeStr = sdf.format(newDateTime);

            if (appointmentRepository.opCheckScheduleConflict(newMedId, newDateTimeStr)) {
                appointmentView.showDialog("Error: El profesional ya tiene una cita programada en ese horario.");
                return;
            }

            existing.setAttDateAndTime(newDateTime);
            existing.setAttMedicalStaffId(newMedId);
            existing.setAttStatus(AppointmentStatus.RESCHEDULED);

            String newReason = appointmentView.getReason();
            if (newReason != null && !newReason.trim().isEmpty()) {
                existing.setAttReason(newReason);
            }

            boolean updated = appointmentRepository.opUpdate(existing);

            if (updated) {
                appointmentView.showDialog("¡Cita reagendada exitosamente! Estado: REAGENDADA");
                opClearReSchedule();
            } else {
                appointmentView.showDialog("Error: No se pudo reagendar la cita.");
            }

        } catch (NumberFormatException e) {
            appointmentView.showDialog("Error: Los campos de ID deben ser numericos.");
        } catch (Exception e) {
            appointmentView.showDialog("Error al reagendar: " + e.getMessage());
        }
    }

    private void opSearchAppointment() {
        try {
            String appointmentIdStr = appointmentView.getAppointmentId();
            if (appointmentIdStr == null || appointmentIdStr.trim().isEmpty()) {
                appointmentView.showDialog("Error: Ingrese el ID de la cita a reagendar.");
                return;
            }

            long appointmentId = Long.parseLong(appointmentIdStr);
            ClsAppointment appointment = appointmentRepository.opView(appointmentId);

            if (appointment == null) {
                appointmentView.showDialog("Error: No se encontro la cita con ID " + appointmentId + ".");
                return;
            }

            // Populate table
            DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID Cita", "ID Paciente", "Teléfono", "ID Medico", "Fecha Hora", "Tipo", "Estado", "Motivo"}, 0);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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

            // Populate reschedule fields
            appointmentView.setMedID(String.valueOf(appointment.getAttMedicalStaffId()));
            appointmentView.setReScheduleDate(appointment.getAttDateAndTime());
            appointmentView.setReason(appointment.getAttReason());

        } catch (NumberFormatException e) {
            appointmentView.showDialog("Error: El ID debe ser numerico.");
        } catch (Exception e) {
            appointmentView.showDialog("Error inesperado: " + e.getMessage());
        }
    }

    private void opExport() {
        try {
            List<ClsAppointment> appointments = appointmentRepository.opListAll();

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
    
    private void opClearSchedule() {
        opClearComponents(appointmentView.getSchedulePanel());
    }

    private void opClearReSchedule() {
        opClearComponents(appointmentView.getReSchedulePanel());
    }

    private void opClearComponents (Component comp) {
        opResetValues(comp);
        
        if (comp instanceof java.awt.Container) {
            for (Component child : ((java.awt.Container) comp).getComponents()) {
                opClearComponents(child);
            }
        }
    }

    private void opResetValues (Component comp) {
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
