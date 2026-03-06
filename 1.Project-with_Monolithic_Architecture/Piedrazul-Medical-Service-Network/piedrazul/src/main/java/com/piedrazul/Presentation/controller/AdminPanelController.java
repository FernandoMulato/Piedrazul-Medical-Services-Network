package com.piedrazul.Presentation.controller;

import com.piedrazul.Presentation.views.AdminPanelView;
import com.piedrazul.Presentation.views.LoginView;

public class AdminPanelController {

  private AdminPanelView view;

  public AdminPanelController(AdminPanelView view) {

    this.view = view;

    view.addUsersListener(e -> manageUsers());
    view.addAppointmentsListener(e -> manageAppointments());
    view.addDoctorsListener(e -> manageDoctors());
    view.addLogoutListener(e -> logout());
  }

  private void manageUsers() {
    System.out.println("Abrir gestión de usuarios");
  }

  private void manageAppointments() {
    System.out.println("Abrir gestión de citas");
  }

  private void manageDoctors() {
    System.out.println("Abrir gestión de médicos");
  }

  private void logout() {

    view.dispose();

    LoginView login = new LoginView();
    new LoginController(login);

    login.setVisible(true);
  }
}