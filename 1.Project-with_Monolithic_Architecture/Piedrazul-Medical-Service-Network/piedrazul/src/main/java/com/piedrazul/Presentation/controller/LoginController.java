package com.piedrazul.Presentation.controller;

import com.piedrazul.Presentation.views.AdminPanelView;
import com.piedrazul.Presentation.views.LoginView;
import com.piedrazul.Services.IUserService;
import com.piedrazul.Domain.enums.Role;

public class LoginController {

  private LoginView loginView;
  private final IUserService userService;
  private final UserController userController;

  public LoginController(LoginView loginView, IUserService userService, UserController userController) {
    this.loginView = loginView;
    this.userService = userService;
    this.userController = userController;

    this.loginView.addLoginListener(e -> login());
  }

  private void login() {

    String username = loginView.getUsername();
    String password = loginView.getPassword();

    try {
      Role roleUser = userService.opVerifyUser(username, password);
      if (roleUser.equals(Role.ADMINISTRATOR)) {
        loginView.dispose(); // cerrar login

        AdminPanelView adminPanel = new AdminPanelView();
        new AdminPanelController(adminPanel, userService, userController);
        adminPanel.setVisible(true);
      }
    } catch (Exception e) {
      loginView.showMessage(e.getMessage());
      return;
    }
  }
}