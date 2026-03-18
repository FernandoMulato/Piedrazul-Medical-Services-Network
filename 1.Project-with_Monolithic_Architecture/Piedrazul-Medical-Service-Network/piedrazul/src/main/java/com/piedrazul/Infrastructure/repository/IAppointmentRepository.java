package com.piedrazul.Infrastructure.repository;

import com.piedrazul.Domain.entities.ClsAppointment;

public interface IAppointmentRepository {
    ClsAppointment opCreate ();
    boolean opUpdate (ClsAppointment appointment);
    void opView (ClsAppointment appointment) 
}
