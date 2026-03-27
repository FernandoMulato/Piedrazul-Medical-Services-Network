package com.piedrazul.Presentation.controller;

import com.piedrazul.Presentation.views.AdminPanelView;
import com.piedrazul.Presentation.views.AppointmentView;
import com.piedrazul.Presentation.views.LoginView;
import com.piedrazul.Services.IAppointmentService;
import com.piedrazul.Services.IUserService;
import com.piedrazul.Domain.enums.Role;

public class LoginController {

  private LoginView loginView;
  private final IUserService userService;
  private final UserController userController;
  private final IAppointmentService appointmentService;

  public LoginController(LoginView loginView, IUserService userService,
      UserController userController, IAppointmentService appointmentService) {
    this.loginView = loginView;
    this.userService = userService;
    this.userController = userController;
    this.appointmentService = appointmentService;

    this.loginView.addLoginListener(e -> login());
  }

  private void login() {

    String username = loginView.getUsername();
    String password = loginView.getPassword();

    try {
      Role roleUser = userService.opVerifyUser(username, password);
      if (roleUser.equals(Role.ADMINISTRATOR)) {
        loginView.dispose();

        AdminPanelView adminPanel = new AdminPanelView();
        new AdminPanelController(adminPanel, userService, userController, appointmentService);
        adminPanel.setVisible(true);
      }
      if (roleUser.equals(Role.APPOINTMENTMANAGER)) {
        loginView.dispose();

        AppointmentView appointmentView = new AppointmentView();
        new AppointmentController(appointmentView, appointmentService);
        appointmentView.setVisible(true);
      }
    } catch (Exception e) {
      loginView.showMessage(e.getMessage());
    }
  }
}