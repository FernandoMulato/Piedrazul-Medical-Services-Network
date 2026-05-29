package com.medical.client.controller;

import com.medical.client.util.AlertHelper;
import com.medical.client.util.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
        // Simple initialization if needed
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            AlertHelper.showError("Error", "Please enter both username and password");
            return;
        }

        // Mock authentication: accept any non-empty input for now
        // A real app would validate against users-service
        NavigationManager.loadMainLayout();
    }
}
