package com.piedrazul.Presentation.controller;

import com.piedrazul.Application.services.IUserService;
import com.piedrazul.Presentation.views.AdminPanelView;
import com.piedrazul.Presentation.views.LoginView;

public class AdminPanelController {

  private AdminPanelView view;
  private final IUserService userService;

  public AdminPanelController(AdminPanelView view, IUserService userService) {

    this.view = view;
    this.userService = userService;

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
    new LoginController(login, userService);

    login.setVisible(true);
  }
}