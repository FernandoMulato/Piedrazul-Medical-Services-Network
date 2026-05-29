package com.medical.client.controller;

import com.medical.client.util.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        NavigationManager.setMainContent(contentArea);
        // Load default view
        NavigationManager.navigateTo("dashboard.fxml");
    }

    @FXML
    public void handleDashboard() {
        NavigationManager.navigateTo("dashboard.fxml");
    }

    @FXML
    public void handleUsers() {
        NavigationManager.navigateTo("user-list.fxml");
    }

    @FXML
    public void handleAppointments() {
        NavigationManager.navigateTo("appointment-list.fxml");
    }

    @FXML
    public void handleClinicalRecords() {
        NavigationManager.navigateTo("clinical-record-list.fxml");
    }

    @FXML
    public void handleLogout() {
        NavigationManager.setRootScene("login.fxml");
    }
}
