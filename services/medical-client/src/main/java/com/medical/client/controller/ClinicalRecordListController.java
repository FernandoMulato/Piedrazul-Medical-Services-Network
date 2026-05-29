package com.medical.client.controller;

import com.medical.client.model.ClinicalRecordResponse;
import com.medical.client.service.ClinicalRecordApiService;
import com.medical.client.util.AlertHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ClinicalRecordListController {

    @FXML private TextField patientIdSearch;
    @FXML private TableView<ClinicalRecordResponse> recordsTable;
    @FXML private TableColumn<ClinicalRecordResponse, Long> idColumn;
    @FXML private TableColumn<ClinicalRecordResponse, String> diagnosisColumn;
    @FXML private TableColumn<ClinicalRecordResponse, String> treatmentColumn;
    @FXML private Label statusLabel;

    private final ClinicalRecordApiService service = new ClinicalRecordApiService();
    private final ObservableList<ClinicalRecordResponse> recordsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        diagnosisColumn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        treatmentColumn.setCellValueFactory(new PropertyValueFactory<>("treatment"));

        recordsTable.setItems(recordsList);
    }

    @FXML
    public void handleSearch() {
        String patientIdStr = patientIdSearch.getText();
        if (patientIdStr == null || patientIdStr.isBlank()) {
            AlertHelper.showError("Error", "Please enter a Patient ID");
            return;
        }

        try {
            Long patientId = Long.parseLong(patientIdStr);
            loadRecords(patientId);
        } catch (NumberFormatException e) {
            AlertHelper.showError("Error", "Patient ID must be a valid number");
        }
    }

    private void loadRecords(Long patientId) {
        statusLabel.setText("Loading records...");
        new Thread(() -> {
            try {
                List<ClinicalRecordResponse> records = service.getByPatient(patientId);
                Platform.runLater(() -> {
                    recordsList.setAll(records);
                    statusLabel.setText("Records loaded: " + records.size());
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Error loading records");
                    AlertHelper.showError("Data Error", "Could not load clinical records: " + e.getMessage());
                });
            }
        }).start();
    }
}
