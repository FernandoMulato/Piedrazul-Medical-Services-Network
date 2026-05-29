package com.medical.client;

import com.medical.client.util.NavigationManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MedicalClientApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Piedrazul Medical Services Network");
        NavigationManager.setPrimaryStage(primaryStage);
        
        // Start at login screen
        NavigationManager.setRootScene("login.fxml");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
