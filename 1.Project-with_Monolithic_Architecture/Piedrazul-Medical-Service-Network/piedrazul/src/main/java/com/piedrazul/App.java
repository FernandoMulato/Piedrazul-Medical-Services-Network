package com.piedrazul;

import javax.swing.SwingUtilities;

import com.piedrazul.Application.services.IUserService;
import com.piedrazul.Application.services.impl.ClsUserServiceImpl;
import com.piedrazul.Infrastructure.config.IDatabaseConnection;
import com.piedrazul.Infrastructure.config.impl.SQLiteConnection;
import com.piedrazul.Infrastructure.repository.IUserRepository;
import com.piedrazul.Infrastructure.repository.impl.ClsSQLiteUserRepository;
import com.piedrazul.Presentation.controller.LoginController;
import com.piedrazul.Presentation.views.LoginView;


public class App {

  public static void main(String[] args) {

    SwingUtilities.invokeLater(() -> {

      LoginView loginView = new LoginView();
      IDatabaseConnection databaseConnection = new SQLiteConnection();
      IUserRepository userRepository = new ClsSQLiteUserRepository(databaseConnection);
      IUserService userService = new ClsUserServiceImpl(userRepository);

      new LoginController(loginView, userService);

      loginView.setVisible(true);

    });
  }
}