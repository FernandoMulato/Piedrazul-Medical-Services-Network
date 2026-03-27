package com.piedrazul.Presentation.controller;

import com.piedrazul.Presentation.views.AdminPanelView;
import com.piedrazul.Presentation.views.AppointmentView;
import com.piedrazul.Presentation.views.LoginView;
import com.piedrazul.Presentation.views.MainFrame;
import com.piedrazul.Services.IAppointmentService;
import com.piedrazul.Services.IUserService;

public class AdminPanelController {

  private AdminPanelView view;
  private final IUserService userService;
  private final UserController userController;
  private final IAppointmentService appointmentService;

  public AdminPanelController(AdminPanelView view, IUserService userService,
      UserController userController, IAppointmentService appointmentService) {
    this.view = view;
    this.userService = userService;
    this.userController = userController;
    this.appointmentService = appointmentService;

    view.addUsersListener(e -> manageUsers());
    view.addAppointmentsListener(e -> manageAppointments());
    view.addDoctorsListener(e -> manageDoctors());
    view.addLogoutListener(e -> logout());
  }

  private void manageUsers() {
    view.dispose();
    MainFrame mainFrame = new MainFrame(userController, userService, appointmentService);
    mainFrame.setVisible(true);
  }

  private void manageAppointments() {
    view.dispose();
    AppointmentView appointmentView = new AppointmentView();
    new AppointmentController(appointmentView, appointmentService);
    appointmentView.setVisible(true);
  }

  private void manageDoctors() {
    System.out.println("Abrir gestión de médicos");
  }

  private void logout() {
    view.dispose();

    LoginView login = new LoginView();
    new LoginController(login, userService, userController, appointmentService);
    login.setVisible(true);
  }
}