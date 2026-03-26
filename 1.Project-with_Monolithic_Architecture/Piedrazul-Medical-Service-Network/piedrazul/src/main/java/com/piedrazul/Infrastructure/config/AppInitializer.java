package com.piedrazul.Infrastructure.config;

import javax.swing.SwingUtilities;

import com.piedrazul.Domain.core.events.ClsClinicEventBus;
import com.piedrazul.Infrastructure.repository.IDatabaseConnection;
import com.piedrazul.Infrastructure.repository.IUserRepository;
import com.piedrazul.Infrastructure.repository.impl.ClsSQLiteUserRepository;
import com.piedrazul.Infrastructure.repository.impl.SQLiteConnection;
import com.piedrazul.Presentation.controller.LoginController;
import com.piedrazul.Presentation.controller.UserController;
import com.piedrazul.Presentation.views.LoginView;
import com.piedrazul.services.IUserService;
import com.piedrazul.services.impl.ClsUserServiceImpl;

public class AppInitializer {

    public void start() {
        // Inicialización de infraestructura
        IDatabaseConnection databaseConnection = new SQLiteConnection();
        IUserRepository userRepository = new ClsSQLiteUserRepository(databaseConnection);
        ClsClinicEventBus eventBus = new ClsClinicEventBus();

        // Inicialización de servicios
        IUserService userService = new ClsUserServiceImpl(userRepository, eventBus);

        // Inicialización de controladores
        UserController userController = new UserController(userService);

        // Inicialización de UI
        initUI(userService, userController);
    }

    private void initUI(IUserService userService, UserController userController) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView, userService, userController);
            loginView.setVisible(true);
        });
    }
}
