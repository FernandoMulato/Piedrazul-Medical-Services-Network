package com.piedrazul.Presentation.controller;

import com.piedrazul.Presentation.views.AdminPanelView;
import com.piedrazul.Presentation.views.LoginView;
import com.piedrazul.Application.services.IUserService;
import com.piedrazul.Domain.enums.Role;

public class LoginController {

  private LoginView loginView;
  private final IUserService userService;

  public LoginController(LoginView loginView , IUserService userService) {
    this.loginView = loginView;
    this.userService = userService;

    // Escuchar botón login
    this.loginView.addLoginListener(e -> login());
  }

  private void login() {

    String username = loginView.getUsername();
    String password = loginView.getPassword();

    try {
      Role roleUser = userService.opVerifyUser(username, password);
      if (roleUser.equals(Role.Administrator)) {
        loginView.dispose(); // cerrar login

        AdminPanelView adminPanel = new AdminPanelView();
        new AdminPanelController(adminPanel, userService);

        adminPanel.setVisible(true);
      }
    } catch (Exception e) {
      loginView.showMessage(e.getMessage());
      return;
    }
  }
}