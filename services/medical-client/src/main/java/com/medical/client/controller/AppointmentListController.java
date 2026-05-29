package com.medical.client.controller;

import com.medical.client.model.AppointmentResponse;
import com.medical.client.model.AppointmentStatus;
import com.medical.client.model.CreateAppointmentRequest;
import com.medical.client.model.UpdateAppointmentRequest;
import com.medical.client.service.AppointmentApiService;
import com.medical.client.util.AlertHelper;
import com.medical.client.util.DialogHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class AppointmentListController {

    @FXML private TableView<AppointmentResponse> appointmentTable;
    @FXML private TableColumn<AppointmentResponse, Long> idColumn;
    @FXML private TableColumn<AppointmentResponse, String> patientColumn;
    @FXML private TableColumn<AppointmentResponse, String> doctorColumn;
    @FXML private TableColumn<AppointmentResponse, LocalDate> dateColumn;
    @FXML private TableColumn<AppointmentResponse, LocalTime> timeColumn;
    @FXML private TableColumn<AppointmentResponse, Integer> durationColumn;
    @FXML private TableColumn<AppointmentResponse, AppointmentStatus> statusColumn;
    @FXML private TextField patientDocSearch;
    @FXML private Label statusLabel;

    private final AppointmentApiService appointmentService = new AppointmentApiService();
    private final ObservableList<AppointmentResponse> appointmentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("professionalName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        appointmentTable.setItems(appointmentList);
        loadAppointments();
    }

    @FXML
    public void loadAppointments() {
        statusLabel.setText("Loading appointments...");
        new Thread(() -> {
            try {
                List<AppointmentResponse> apps = appointmentService.getAllAppointments();
                Platform.runLater(() -> {
                    appointmentList.setAll(apps);
                    statusLabel.setText("Appointments loaded: " + apps.size());
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Error loading appointments");
                    AlertHelper.showError("Data Error", "Could not load appointments: " + e.getMessage());
                });
            }
        }).start();
    }

    @FXML
    public void handleSearchByPatient() {
        String doc = patientDocSearch.getText();
        if (doc == null || doc.isBlank()) {
            AlertHelper.showError("Validation", "Please enter a patient document number.");
            return;
        }

        statusLabel.setText("Searching...");
        new Thread(() -> {
            try {
                List<AppointmentResponse> apps = appointmentService.getAppointmentsByPatient(doc.trim());
                Platform.runLater(() -> {
                    appointmentList.setAll(apps);
                    statusLabel.setText("Results for '" + doc.trim() + "': " + apps.size());
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Error searching");
                    AlertHelper.showError("Error", "Could not search appointments: " + e.getMessage());
                });
            }
        }).start();
    }

    @FXML
    public void handleCreateAppointment() {
        Dialog<CreateAppointmentRequest> dialog = DialogHelper.createStyledDialog("Create New Appointment");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = DialogHelper.createFormGrid();

        TextField patientDoc = DialogHelper.addTextField(grid, 0, "Patient Document *", "Document number");
        TextField patientName = DialogHelper.addTextField(grid, 1, "Patient Name", "Full name");
        TextField patientPhone = DialogHelper.addTextField(grid, 2, "Patient Phone", "Phone number");
        TextField profId = DialogHelper.addTextField(grid, 3, "Professional ID", "ID number");
        TextField profName = DialogHelper.addTextField(grid, 4, "Professional Name", "Doctor name");
        DatePicker datePicker = DialogHelper.addDatePicker(grid, 5, "Date *");
        TextField time = DialogHelper.addTextField(grid, 6, "Time *", "HH:mm (e.g. 09:30)");
        TextField duration = DialogHelper.addTextField(grid, 7, "Duration (min)", "30");
        TextArea reason = DialogHelper.addTextArea(grid, 8, "Reason", "Reason for appointment");

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                CreateAppointmentRequest req = new CreateAppointmentRequest();
                req.setPatientDocument(patientDoc.getText().trim());
                req.setPatientName(DialogHelper.getOrNull(patientName.getText()));
                req.setPatientPhone(DialogHelper.getOrNull(patientPhone.getText()));
                String profIdStr = profId.getText().trim();
                if (!profIdStr.isBlank()) {
                    try { req.setProfessionalId(Long.parseLong(profIdStr)); } catch (NumberFormatException ignored) {}
                }
                req.setProfessionalName(DialogHelper.getOrNull(profName.getText()));
                req.setDate(datePicker.getValue() != null ? datePicker.getValue().toString() : null);
                req.setTime(DialogHelper.getOrNull(time.getText()));
                String durStr = duration.getText().trim();
                if (!durStr.isBlank()) {
                    try { req.setDurationMinutes(Integer.parseInt(durStr)); } catch (NumberFormatException ignored) {}
                }
                req.setReason(DialogHelper.getOrNull(reason.getText()));
                return req;
            }
            return null;
        });

        Optional<CreateAppointmentRequest> result = dialog.showAndWait();
        result.ifPresent(req -> {
            if (req.getPatientDocument() == null || req.getPatientDocument().isBlank()
                    || req.getDate() == null || req.getTime() == null) {
                AlertHelper.showError("Validation", "Patient document, date, and time are required.");
                return;
            }
            statusLabel.setText("Creating appointment...");
            new Thread(() -> {
                try {
                    AppointmentResponse created = appointmentService.createAppointment(req);
                    Platform.runLater(() -> {
                        loadAppointments();
                        AlertHelper.showSuccess("Success", "Appointment created with ID: " + created.getId());
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Error creating appointment");
                        AlertHelper.showError("Error", "Could not create appointment: " + e.getMessage());
                    });
                }
            }).start();
        });
    }

    @FXML
    public void handleRescheduleAppointment() {
        AppointmentResponse selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection", "Please select an appointment to reschedule.");
            return;
        }

        Dialog<UpdateAppointmentRequest> dialog = DialogHelper.createStyledDialog("Reschedule Appointment - ID: " + selected.getId());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = DialogHelper.createFormGrid();

        DatePicker datePicker = DialogHelper.addDatePicker(grid, 0, "New Date", selected.getDate());
        TextField time = DialogHelper.addTextField(grid, 1, "New Time", "HH:mm",
                selected.getTime() != null ? selected.getTime().toString() : "");

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                UpdateAppointmentRequest req = new UpdateAppointmentRequest();
                req.setDate(datePicker.getValue() != null ? datePicker.getValue().toString() : null);
                req.setTime(DialogHelper.getOrNull(time.getText()));
                return req;
            }
            return null;
        });

        Optional<UpdateAppointmentRequest> result = dialog.showAndWait();
        result.ifPresent(req -> {
            statusLabel.setText("Rescheduling...");
            new Thread(() -> {
                try {
                    appointmentService.updateAppointment(selected.getId(), req);
                    Platform.runLater(() -> {
                        loadAppointments();
                        AlertHelper.showSuccess("Success", "Appointment rescheduled successfully.");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Error rescheduling");
                        AlertHelper.showError("Error", "Could not reschedule: " + e.getMessage());
                    });
                }
            }).start();
        });
    }

    @FXML
    public void handleCancelAppointment() {
        AppointmentResponse selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection", "Please select an appointment to cancel.");
            return;
        }

        boolean confirmed = AlertHelper.showConfirm("Confirm Cancellation",
                "Are you sure you want to cancel appointment ID " + selected.getId() + "?");
        if (!confirmed) return;

        statusLabel.setText("Cancelling appointment...");
        new Thread(() -> {
            try {
                appointmentService.cancelAppointment(selected.getId());
                Platform.runLater(() -> {
                    loadAppointments();
                    AlertHelper.showSuccess("Success", "Appointment cancelled successfully.");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Error cancelling");
                    AlertHelper.showError("Error", "Could not cancel appointment: " + e.getMessage());
                });
            }
        }).start();
    }
}
