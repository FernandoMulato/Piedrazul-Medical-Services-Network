package com.piedrazul.Application.services;

import java.util.List;

import com.piedrazul.Domain.entities.ClsAppointment;

public interface IAppointmentService {
    ClsAppointment opSchedule(ClsAppointment appointment);
    boolean opReSchedule(ClsAppointment appointment);
    ClsAppointment opFindAppointment(long id);
    boolean opCheckScheduleConflict(long medId, String dateTime);
    List<ClsAppointment> opListAll();
}
