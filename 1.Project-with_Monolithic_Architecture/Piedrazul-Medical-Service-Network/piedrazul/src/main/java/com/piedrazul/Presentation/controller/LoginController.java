package com.piedrazul.Presentation.controller;

import com.piedrazul.Presentation.views.AdminPanelView;
import com.piedrazul.Presentation.views.LoginView;
import com.piedrazul.Application.services.IUserService;
import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Application.services.impl.ClsUserServiceImpl;

public class LoginController {

  private LoginView loginView;
  private IUserService userService = new ClsUserServiceImpl();

  public LoginController(LoginView loginView) {
    this.loginView = loginView;

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
        new AdminPanelController(adminPanel);

        adminPanel.setVisible(true);
      }
    } catch (Exception e) {
      loginView.showMessage(e.getMessage());
      return;
    }
  }
}