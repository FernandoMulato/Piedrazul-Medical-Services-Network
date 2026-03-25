package com.piedrazul;

import com.piedrazul.Infrastructure.repository.impl.ClsAppointmentRepository;
import com.piedrazul.Infrastructure.config.impl.SQLiteConnection;
import com.piedrazul.Domain.entities.ClsAppointment;

public class Debug {
    public static void main(String[] args) {
        try {
            SQLiteConnection conn = new SQLiteConnection();
            ClsAppointmentRepository repo = new ClsAppointmentRepository(conn);
            
            System.out.println("Checking conflict");
            boolean conflict = repo.opCheckScheduleConflict(1, "2026-03-18 10:00");
            System.out.println("Conflict? " + conflict);
            
            System.out.println("Fetching appointment 1");
            ClsAppointment app = repo.opView(1);
            System.out.println("App id 1: " + app);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
