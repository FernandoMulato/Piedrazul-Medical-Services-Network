package com.medical.client.controller;

import com.medical.client.model.CreateUserRequest;
import com.medical.client.model.UpdateUserRequest;
import com.medical.client.model.UserResponse;
import com.medical.client.model.UserRole;
import com.medical.client.service.UserApiService;
import com.medical.client.util.AlertHelper;
import com.medical.client.util.DialogHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Optional;

public class UserListController {

    @FXML private TableView<UserResponse> userTable;
    @FXML private TableColumn<UserResponse, Long> idColumn;
    @FXML private TableColumn<UserResponse, String> usernameColumn;
    @FXML private TableColumn<UserResponse, String> emailColumn;
    @FXML private TableColumn<UserResponse, String> nameColumn;
    @FXML private TableColumn<UserResponse, String> roleColumn;
    @FXML private TableColumn<UserResponse, Boolean> activeColumn;
    @FXML private Label statusLabel;

    private final UserApiService userService = new UserApiService();
    private final ObservableList<UserResponse> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nameColumn.setCellValueFactory(cellData -> {
            UserResponse u = cellData.getValue();
            String name = u.getFullName();
            return new javafx.beans.property.SimpleStringProperty(name);
        });
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

    @FXML
    public void handleCreateUser() {
        Dialog<CreateUserRequest> dialog = DialogHelper.createStyledDialog("Create New User");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = DialogHelper.createFormGrid();

        TextField username = DialogHelper.addTextField(grid, 0, "Username *", "Min 4 characters");
        PasswordField password = DialogHelper.addPasswordField(grid, 1, "Password *", "Min 8 characters");
        TextField email = DialogHelper.addTextField(grid, 2, "Email *", "user@example.com");
        ComboBox<UserRole> role = DialogHelper.addComboBox(grid, 3, "Role *", UserRole.values());
        TextField firstName = DialogHelper.addTextField(grid, 4, "First Name", "First name");
        TextField lastName = DialogHelper.addTextField(grid, 5, "Last Name", "Last name");
        TextField docType = DialogHelper.addTextField(grid, 6, "Document Type", "CC, TI, etc.");
        TextField docNumber = DialogHelper.addTextField(grid, 7, "Document Number", "Document number");
        TextField phone = DialogHelper.addTextField(grid, 8, "Phone", "Phone number");
        TextField address = DialogHelper.addTextField(grid, 9, "Address", "Address");
        TextField eps = DialogHelper.addTextField(grid, 10, "EPS", "EPS name");
        TextField specialty = DialogHelper.addTextField(grid, 11, "Specialty", "For professionals");
        TextField license = DialogHelper.addTextField(grid, 12, "License Number", "For professionals");

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                CreateUserRequest req = new CreateUserRequest();
                req.setUsername(username.getText().trim());
                req.setPassword(password.getText());
                req.setEmail(email.getText().trim());
                req.setRole(role.getValue());
                req.setFirstName(DialogHelper.getOrNull(firstName.getText()));
                req.setLastName(DialogHelper.getOrNull(lastName.getText()));
                req.setDocumentType(DialogHelper.getOrNull(docType.getText()));
                req.setDocumentNumber(DialogHelper.getOrNull(docNumber.getText()));
                req.setPhone(DialogHelper.getOrNull(phone.getText()));
                req.setAddress(DialogHelper.getOrNull(address.getText()));
                req.setEps(DialogHelper.getOrNull(eps.getText()));
                req.setSpecialty(DialogHelper.getOrNull(specialty.getText()));
                req.setLicenseNumber(DialogHelper.getOrNull(license.getText()));
                return req;
            }
            return null;
        });

        Optional<CreateUserRequest> result = dialog.showAndWait();
        result.ifPresent(req -> {
            if (req.getUsername().isBlank() || req.getPassword().isBlank() || req.getEmail().isBlank() || req.getRole() == null) {
                AlertHelper.showError("Validation", "Username, password, email, and role are required.");
                return;
            }
            statusLabel.setText("Creating user...");
            new Thread(() -> {
                try {
                    UserResponse created = userService.createUser(req);
                    Platform.runLater(() -> {
                        loadUsers();
                        AlertHelper.showSuccess("Success", "User created: " + created.getUsername());
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Error creating user");
                        AlertHelper.showError("Error", "Could not create user: " + e.getMessage());
                    });
                }
            }).start();
        });
    }

    @FXML
    public void handleEditUser() {
        UserResponse selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection", "Please select a user to edit.");
            return;
        }

        Dialog<UpdateUserRequest> dialog = DialogHelper.createStyledDialog("Edit User - ID: " + selected.getId());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = DialogHelper.createFormGrid();

        TextField username = DialogHelper.addTextField(grid, 0, "Username", "Username", selected.getUsername());
        PasswordField password = DialogHelper.addPasswordField(grid, 1, "New Password", "Leave blank to keep current");
        TextField email = DialogHelper.addTextField(grid, 2, "Email", "Email", selected.getEmail());
        ComboBox<UserRole> role = DialogHelper.addComboBox(grid, 3, "Role", UserRole.values());
        role.setValue(selected.getRole());
        TextField firstName = DialogHelper.addTextField(grid, 4, "First Name", "First name", selected.getFirstName());
        TextField lastName = DialogHelper.addTextField(grid, 5, "Last Name", "Last name", selected.getLastName());
        TextField docType = DialogHelper.addTextField(grid, 6, "Document Type", "CC, TI", selected.getDocumentType());
        TextField docNumber = DialogHelper.addTextField(grid, 7, "Document Number", "Number", selected.getDocumentNumber());
        TextField phone = DialogHelper.addTextField(grid, 8, "Phone", "Phone", selected.getPhone());
        TextField address = DialogHelper.addTextField(grid, 9, "Address", "Address", selected.getAddress());
        TextField eps = DialogHelper.addTextField(grid, 10, "EPS", "EPS", selected.getEps());
        TextField specialty = DialogHelper.addTextField(grid, 11, "Specialty", "Specialty", selected.getSpecialty());
        TextField license = DialogHelper.addTextField(grid, 12, "License", "License", selected.getLicenseNumber());

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                UpdateUserRequest req = new UpdateUserRequest();
                req.setUsername(DialogHelper.getOrNull(username.getText()));
                req.setPassword(DialogHelper.getOrNull(password.getText()));
                req.setEmail(DialogHelper.getOrNull(email.getText()));
                req.setRole(role.getValue());
                req.setFirstName(DialogHelper.getOrNull(firstName.getText()));
                req.setLastName(DialogHelper.getOrNull(lastName.getText()));
                req.setDocumentType(DialogHelper.getOrNull(docType.getText()));
                req.setDocumentNumber(DialogHelper.getOrNull(docNumber.getText()));
                req.setPhone(DialogHelper.getOrNull(phone.getText()));
                req.setAddress(DialogHelper.getOrNull(address.getText()));
                req.setEps(DialogHelper.getOrNull(eps.getText()));
                req.setSpecialty(DialogHelper.getOrNull(specialty.getText()));
                req.setLicenseNumber(DialogHelper.getOrNull(license.getText()));
                return req;
            }
            return null;
        });

        Optional<UpdateUserRequest> result = dialog.showAndWait();
        result.ifPresent(req -> {
            statusLabel.setText("Updating user...");
            new Thread(() -> {
                try {
                    userService.updateUser(selected.getId(), req);
                    Platform.runLater(() -> {
                        loadUsers();
                        AlertHelper.showSuccess("Success", "User updated successfully.");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Error updating user");
                        AlertHelper.showError("Error", "Could not update user: " + e.getMessage());
                    });
                }
            }).start();
        });
    }

    @FXML
    public void handleDeactivateUser() {
        UserResponse selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection", "Please select a user to deactivate.");
            return;
        }

        boolean confirmed = AlertHelper.showConfirm("Confirm Deactivation",
                "Are you sure you want to deactivate user '" + selected.getUsername() + "'?");
        if (!confirmed) return;

        statusLabel.setText("Deactivating user...");
        new Thread(() -> {
            try {
                userService.deactivateUser(selected.getId());
                Platform.runLater(() -> {
                    loadUsers();
                    AlertHelper.showSuccess("Success", "User deactivated successfully.");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Error deactivating user");
                    AlertHelper.showError("Error", "Could not deactivate user: " + e.getMessage());
                });
            }
        }).start();
    }
}
