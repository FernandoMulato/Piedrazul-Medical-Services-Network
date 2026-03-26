package com.piedrazul.Presentation.controller;

import com.piedrazul.Presentation.views.AdminPanelView;
import com.piedrazul.Presentation.views.LoginView;
import com.piedrazul.Presentation.views.MainFrame;
import com.piedrazul.Services.IUserService;

public class AdminPanelController {

  private AdminPanelView view;
  private final IUserService userService;
  private final UserController userController;

  public AdminPanelController(AdminPanelView view, IUserService userService, UserController userController) {
    this.view = view;
    this.userService = userService;
    this.userController = userController;

    view.addUsersListener(e -> manageUsers());
    view.addAppointmentsListener(e -> manageAppointments());
    view.addDoctorsListener(e -> manageDoctors());
    view.addLogoutListener(e -> logout());
  }

  private void manageUsers() {
    view.dispose();
    MainFrame mainFrame = new MainFrame(userController);
    mainFrame.setVisible(true);
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
    new LoginController(login, userService, userController);
    login.setVisible(true);
  }
}