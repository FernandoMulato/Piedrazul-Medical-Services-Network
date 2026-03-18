package com.piedrazul;

import javax.swing.SwingUtilities;

import com.piedrazul.Infrastructure.config.impl.SQLiteConnection;
import com.piedrazul.Infrastructure.repository.IAppointmentRepository;
import com.piedrazul.Infrastructure.repository.impl.ClsAppointmentRepository;
import com.piedrazul.Presentation.controller.AppointmentController;
import com.piedrazul.Presentation.controller.LoginController;
import com.piedrazul.Presentation.views.AppointmentView;
import com.piedrazul.Presentation.views.LoginView;

public class App {

  public static void main(String[] args) {

    SwingUtilities.invokeLater(() -> {

      //LoginView loginView = new LoginView();

      //new LoginController(loginView);

      //loginView.setVisible(true);

      SQLiteConnection dbConnection = new SQLiteConnection();
      IAppointmentRepository appointmentRepository = new ClsAppointmentRepository(dbConnection);

      AppointmentView appointmentView = new AppointmentView();
      new AppointmentController(appointmentView, appointmentRepository);
      appointmentView.setVisible(true);

    });
  }
}