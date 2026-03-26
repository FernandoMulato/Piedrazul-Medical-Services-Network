package com.piedrazul.Infrastructure.repository;

import java.util.List;

import com.piedrazul.Domain.entities.ClsAppointment;

public interface IAppointmentRepository {
    ClsAppointment opCreate(ClsAppointment appointment);
    boolean opUpdate(ClsAppointment appointment);
    ClsAppointment opView(long id);
    boolean opCheckScheduleConflict(long medId, String dateTime);
    List<ClsAppointment> opListAll();
}