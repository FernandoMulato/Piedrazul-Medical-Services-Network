package com.piedrazul;

import javax.swing.SwingUtilities;

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

      AppointmentView appointmentView = new AppointmentView();
      new AppointmentController(appointmentView);
      appointmentView.setVisible(true);

    });
  }
}