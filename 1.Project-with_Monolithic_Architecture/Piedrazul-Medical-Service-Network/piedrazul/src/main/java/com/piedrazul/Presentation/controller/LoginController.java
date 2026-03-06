package com.piedrazul.Presentation.controller;

import com.piedrazul.Presentation.views.AdminPanelView;
import com.piedrazul.Presentation.views.LoginView;

public class LoginController {

  private LoginView loginView;

  public LoginController(LoginView loginView) {
    this.loginView = loginView;

    // Escuchar botón login
    this.loginView.addLoginListener(e -> login());
  }

  private void login() {

    String username = loginView.getUsername();
    String password = loginView.getPassword();

    // Aquí luego conectarás tu servicio o base de datos
    if (username.equals("admin") && password.equals("1234")) {

      loginView.dispose(); // cerrar login

      AdminPanelView adminPanel = new AdminPanelView();
      new AdminPanelController(adminPanel);

      adminPanel.setVisible(true);

    } else {

      loginView.showMessage("Usuario o contraseña incorrectos");

    }
  }
}