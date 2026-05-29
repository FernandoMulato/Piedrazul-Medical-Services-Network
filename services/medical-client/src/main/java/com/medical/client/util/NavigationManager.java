package com.medical.client.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationManager {

    private static StackPane mainContent;
    private static Stage primaryStage;

    public static void setMainContent(StackPane content) {
        mainContent = content;
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource("/fxml/" + fxmlFile));
            Node node = loader.load();
            mainContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showError("Navigation Error", "Could not load view: " + fxmlFile);
        }
    }
    
    public static void loadMainLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1024, 768);
            scene.getStylesheets().add(NavigationManager.class.getResource("/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showError("Navigation Error", "Could not load main layout");
        }
    }
    
    // For navigating directly on the root scene (e.g. login)
    public static void setRootScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource("/fxml/" + fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1024, 768);
            scene.getStylesheets().add(NavigationManager.class.getResource("/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showError("Navigation Error", "Could not load view: " + fxmlFile);
        }
    }
}
