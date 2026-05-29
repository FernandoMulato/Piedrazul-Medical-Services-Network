package com.medical.client.controller;

import com.medical.client.model.AppointmentResponse;
import com.medical.client.service.AppointmentApiService;
import com.medical.client.util.AlertHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class AppointmentListController {

    @FXML private TableView<AppointmentResponse> appointmentTable;
    @FXML private TableColumn<AppointmentResponse, Long> idColumn;
    @FXML private TableColumn<AppointmentResponse, String> patientColumn;
    @FXML private TableColumn<AppointmentResponse, String> doctorColumn;
    @FXML private TableColumn<AppointmentResponse, LocalDate> dateColumn;
    @FXML private TableColumn<AppointmentResponse, String> statusColumn;
    @FXML private Label statusLabel;

    private final AppointmentApiService appointmentService = new AppointmentApiService();
    private final ObservableList<AppointmentResponse> appointmentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("professionalName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
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
}
