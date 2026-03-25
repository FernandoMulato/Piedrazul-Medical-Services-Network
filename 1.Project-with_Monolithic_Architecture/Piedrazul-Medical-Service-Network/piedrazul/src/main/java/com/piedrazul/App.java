package com.piedrazul;

import javax.swing.SwingUtilities;

import com.piedrazul.Infrastructure.config.impl.SQLiteConnection;
import com.piedrazul.Infrastructure.repository.IAppointmentRepository;
import com.piedrazul.Infrastructure.repository.impl.ClsAppointmentRepository;
import com.piedrazul.Presentation.controller.AppointmentController;
import com.piedrazul.Application.services.IUserService;
import com.piedrazul.Application.services.impl.ClsUserServiceImpl;
import com.piedrazul.Domain.core.events.ClsClinicEventBus;
import com.piedrazul.Infrastructure.config.IDatabaseConnection;
import com.piedrazul.Infrastructure.config.impl.SQLiteConnection;
import com.piedrazul.Infrastructure.repository.IUserRepository;
import com.piedrazul.Infrastructure.repository.impl.ClsSQLiteUserRepository;
import com.piedrazul.Presentation.controller.LoginController;
import com.piedrazul.Presentation.controller.UserController;
import com.piedrazul.Presentation.views.LoginView;

public class App {

  public static void main(String[] args) {

    SwingUtilities.invokeLater(() -> {

      IDatabaseConnection databaseConnection = new SQLiteConnection();
      IUserRepository userRepository = new ClsSQLiteUserRepository(databaseConnection);
      ClsClinicEventBus eventBus = new ClsClinicEventBus();
      IUserService userService = new ClsUserServiceImpl(userRepository, eventBus);
      UserController userController = new UserController(userService);

      SwingUtilities.invokeLater(() -> {
        LoginView loginView = new LoginView();
        new LoginController(loginView, userService, userController);
        loginView.setVisible(true);
      });

    });
  }
}