package com.medical.client.controller;

import com.medical.client.model.UserResponse;
import com.medical.client.service.UserApiService;
import com.medical.client.util.AlertHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class UserListController {

    @FXML private TableView<UserResponse> userTable;
    @FXML private TableColumn<UserResponse, Long> idColumn;
    @FXML private TableColumn<UserResponse, String> usernameColumn;
    @FXML private TableColumn<UserResponse, String> roleColumn;
    @FXML private TableColumn<UserResponse, Boolean> activeColumn;
    @FXML private Label statusLabel;

    private final UserApiService userService = new UserApiService();
    private final ObservableList<UserResponse> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));

        userTable.setItems(userList);
        loadUsers();
    }

    @FXML
    public void loadUsers() {
        statusLabel.setText("Loading users...");
        new Thread(() -> {
            try {
                List<UserResponse> users = userService.getAllUsers();
                Platform.runLater(() -> {
                    userList.setAll(users);
                    statusLabel.setText("Users loaded: " + users.size());
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Error loading users");
                    AlertHelper.showError("Data Error", "Could not load users: " + e.getMessage());
                });
            }
        }).start();
    }
}
