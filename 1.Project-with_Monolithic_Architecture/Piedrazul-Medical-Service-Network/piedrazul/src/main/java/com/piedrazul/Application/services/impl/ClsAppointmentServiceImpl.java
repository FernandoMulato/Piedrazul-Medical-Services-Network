package com.piedrazul.Application.services.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import com.piedrazul.Application.services.IAppointmentService;
import com.piedrazul.Domain.entities.ClsAppointment;
import com.piedrazul.Domain.enums.AttentionType;
import com.piedrazul.Infrastructure.repository.IAppointmentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClsAppointmentServiceImpl implements IAppointmentService {

    private final IAppointmentRepository appointmentRepository;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public ClsAppointment opSchedule(ClsAppointment appointment) {
        if (appointment.getAttCitizenshipCardPatient() <= 0
                || appointment.getAttPhoneNumber() == null || appointment.getAttPhoneNumber().trim().isEmpty()
                || appointment.getAttMedicalStaffId() <= 0
                || appointment.getAttReason() == null || appointment.getAttReason().trim().isEmpty()
                || appointment.getAttDateAndTime() == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

        String dateTimeStr = SDF.format(appointment.getAttDateAndTime());

        if (appointmentRepository.opCheckScheduleConflict(appointment.getAttMedicalStaffId(), dateTimeStr)) {
            throw new IllegalStateException("El profesional ya tiene una cita programada en ese horario.");
        }

        ClsAppointment created = appointmentRepository.opCreate(appointment);

        if (created == null || created.getAttId() <= 0) {
            throw new RuntimeException("No se pudo guardar la cita.");
        }

        return created;
    }

    @Override
    public boolean opReSchedule(ClsAppointment appointment) {
        if (appointment.getAttId() <= 0) {
            throw new IllegalArgumentException("El ID de la cita es obligatorio.");
        }

        ClsAppointment existing = appointmentRepository.opView(appointment.getAttId());
        if (existing == null) {
            throw new IllegalArgumentException("No se encontró la cita con ID " + appointment.getAttId() + ".");
        }

        long newMedId = appointment.getAttMedicalStaffId() > 0
                ? appointment.getAttMedicalStaffId()
                : existing.getAttMedicalStaffId();

        String newDateTimeStr = SDF.format(appointment.getAttDateAndTime());

        if (appointmentRepository.opCheckScheduleConflict(newMedId, newDateTimeStr)) {
            throw new IllegalStateException("El profesional ya tiene una cita programada en ese horario.");
        }

        existing.setAttDateAndTime(appointment.getAttDateAndTime());
        existing.setAttMedicalStaffId(newMedId);
        existing.setAttStatus(com.piedrazul.Domain.enums.AppointmentStatus.RESCHEDULED);

        if (appointment.getAttReason() != null && !appointment.getAttReason().trim().isEmpty()) {
            existing.setAttReason(appointment.getAttReason());
        }

        return appointmentRepository.opUpdate(existing);
    }

    @Override
    public ClsAppointment opFindAppointment(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID de la cita debe ser mayor que cero.");
        }

        ClsAppointment appointment = appointmentRepository.opView(id);
        if (appointment == null) {
            throw new IllegalArgumentException("No se encontró la cita con ID " + id + ".");
        }

        return appointment;
    }

    @Override
    public boolean opCheckScheduleConflict(long medId, String dateTime) {
        return appointmentRepository.opCheckScheduleConflict(medId, dateTime);
    }

    @Override
    public List<ClsAppointment> opListAll() {
        return appointmentRepository.opListAll();
    }

    public static AttentionType resolveAttentionType(String especialidad) {
        return "Neuroterapia".equals(especialidad) ? AttentionType.SPECIALIZED : AttentionType.GENERAL;
    }
}
