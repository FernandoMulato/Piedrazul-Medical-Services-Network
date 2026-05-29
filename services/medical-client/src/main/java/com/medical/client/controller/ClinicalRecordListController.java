package com.medical.client.controller;

import com.medical.client.model.ClinicalRecordResponse;
import com.medical.client.model.CreateClinicalRecordRequest;
import com.medical.client.model.UpdateClinicalRecordRequest;
import com.medical.client.service.ClinicalRecordApiService;
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
import java.util.List;
import java.util.Optional;

public class ClinicalRecordListController {

    @FXML private TextField patientIdSearch;
    @FXML private TableView<ClinicalRecordResponse> recordsTable;
    @FXML private TableColumn<ClinicalRecordResponse, Long> idColumn;
    @FXML private TableColumn<ClinicalRecordResponse, Long> patientIdColumn;
    @FXML private TableColumn<ClinicalRecordResponse, Long> professionalIdColumn;
    @FXML private TableColumn<ClinicalRecordResponse, String> diagnosisColumn;
    @FXML private TableColumn<ClinicalRecordResponse, String> treatmentColumn;
    @FXML private TableColumn<ClinicalRecordResponse, LocalDate> dateColumn;
    @FXML private Label statusLabel;

    private final ClinicalRecordApiService service = new ClinicalRecordApiService();
    private final ObservableList<ClinicalRecordResponse> recordsList = FXCollections.observableArrayList();
    private Long currentPatientId = null;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        professionalIdColumn.setCellValueFactory(new PropertyValueFactory<>("professionalId"));
        diagnosisColumn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        treatmentColumn.setCellValueFactory(new PropertyValueFactory<>("treatment"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("consultationDate"));

        recordsTable.setItems(recordsList);
    }

    @FXML
    public void handleSearch() {
        String patientIdStr = patientIdSearch.getText();
        if (patientIdStr == null || patientIdStr.isBlank()) {
            AlertHelper.showError("Validation", "Please enter a Patient ID");
            return;
        }

        try {
            Long patientId = Long.parseLong(patientIdStr);
            loadRecords(patientId);
        } catch (NumberFormatException e) {
            AlertHelper.showError("Validation", "Patient ID must be a valid number");
        }
    }

    private void loadRecords(Long patientId) {
        statusLabel.setText("Loading records...");
        new Thread(() -> {
            try {
                List<ClinicalRecordResponse> records = service.getByPatient(patientId);
                Platform.runLater(() -> {
                    recordsList.setAll(records);
                    currentPatientId = patientId;
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

    @FXML
    public void handleCreateRecord() {
        Dialog<CreateClinicalRecordRequest> dialog = DialogHelper.createStyledDialog("Create New Clinical Record");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = DialogHelper.createFormGrid();

        TextField patientIdField = DialogHelper.addTextField(grid, 0, "Patient ID *", "Patient ID", currentPatientId != null ? currentPatientId.toString() : "");
        TextField profIdField = DialogHelper.addTextField(grid, 1, "Professional ID *", "Professional ID");
        TextField profTypeField = DialogHelper.addTextField(grid, 2, "Professional Type *", "GENERAL_PRACTITIONER, SPECIALIST");
        DatePicker datePicker = DialogHelper.addDatePicker(grid, 3, "Consultation Date *", LocalDate.now());
        TextArea diagnosisArea = DialogHelper.addTextArea(grid, 4, "Diagnosis *", "Enter diagnosis");
        TextArea treatmentArea = DialogHelper.addTextArea(grid, 5, "Treatment *", "Enter treatment");
        TextArea observationsArea = DialogHelper.addTextArea(grid, 6, "Observations", "Additional notes");

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                CreateClinicalRecordRequest req = new CreateClinicalRecordRequest();
                try {
                    req.setPatientId(Long.parseLong(patientIdField.getText().trim()));
                    req.setProfessionalId(Long.parseLong(profIdField.getText().trim()));
                } catch (NumberFormatException ignored) {}
                req.setProfessionalType(DialogHelper.getOrNull(profTypeField.getText()));
                req.setConsultationDate(datePicker.getValue());
                req.setDiagnosis(DialogHelper.getOrNull(diagnosisArea.getText()));
                req.setTreatment(DialogHelper.getOrNull(treatmentArea.getText()));
                req.setObservations(DialogHelper.getOrNull(observationsArea.getText()));
                return req;
            }
            return null;
        });

        Optional<CreateClinicalRecordRequest> result = dialog.showAndWait();
        result.ifPresent(req -> {
            if (req.getPatientId() == null || req.getProfessionalId() == null || req.getProfessionalType() == null ||
                req.getConsultationDate() == null || req.getDiagnosis() == null || req.getTreatment() == null) {
                AlertHelper.showError("Validation", "Patient ID, Professional ID, Type, Date, Diagnosis, and Treatment are required.");
                return;
            }
            statusLabel.setText("Creating record...");
            new Thread(() -> {
                try {
                    ClinicalRecordResponse created = service.create(req);
                    Platform.runLater(() -> {
                        if (currentPatientId != null && currentPatientId.equals(created.getPatientId())) {
                            loadRecords(currentPatientId);
                        } else {
                            patientIdSearch.setText(created.getPatientId().toString());
                            loadRecords(created.getPatientId());
                        }
                        AlertHelper.showSuccess("Success", "Clinical record created with ID: " + created.getId());
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Error creating record");
                        AlertHelper.showError("Error", "Could not create record: " + e.getMessage());
                    });
                }
            }).start();
        });
    }

    @FXML
    public void handleEditRecord() {
        ClinicalRecordResponse selected = recordsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection", "Please select a record to edit.");
            return;
        }

        Dialog<UpdateClinicalRecordRequest> dialog = DialogHelper.createStyledDialog("Edit Clinical Record - ID: " + selected.getId());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = DialogHelper.createFormGrid();

        TextArea diagnosisArea = DialogHelper.addTextArea(grid, 0, "Diagnosis", "Diagnosis", selected.getDiagnosis());
        TextArea treatmentArea = DialogHelper.addTextArea(grid, 1, "Treatment", "Treatment", selected.getTreatment());
        TextArea observationsArea = DialogHelper.addTextArea(grid, 2, "Observations", "Observations", selected.getObservations());

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                UpdateClinicalRecordRequest req = new UpdateClinicalRecordRequest();
                req.setDiagnosis(DialogHelper.getOrNull(diagnosisArea.getText()));
                req.setTreatment(DialogHelper.getOrNull(treatmentArea.getText()));
                req.setObservations(DialogHelper.getOrNull(observationsArea.getText()));
                return req;
            }
            return null;
        });

        Optional<UpdateClinicalRecordRequest> result = dialog.showAndWait();
        result.ifPresent(req -> {
            statusLabel.setText("Updating record...");
            new Thread(() -> {
                try {
                    service.update(selected.getId(), req);
                    Platform.runLater(() -> {
                        if (currentPatientId != null) {
                            loadRecords(currentPatientId);
                        }
                        AlertHelper.showSuccess("Success", "Clinical record updated successfully.");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Error updating record");
                        AlertHelper.showError("Error", "Could not update record: " + e.getMessage());
                    });
                }
            }).start();
        });
    }

    @FXML
    public void handleDeleteRecord() {
        ClinicalRecordResponse selected = recordsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection", "Please select a record to delete.");
            return;
        }

        boolean confirmed = AlertHelper.showConfirm("Confirm Deletion",
                "Are you sure you want to delete clinical record ID " + selected.getId() + "?");
        if (!confirmed) return;

        statusLabel.setText("Deleting record...");
        new Thread(() -> {
            try {
                service.delete(selected.getId());
                Platform.runLater(() -> {
                    if (currentPatientId != null) {
                        loadRecords(currentPatientId);
                    }
                    AlertHelper.showSuccess("Success", "Clinical record deleted successfully.");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Error deleting record");
                    AlertHelper.showError("Error", "Could not delete record: " + e.getMessage());
                });
            }
        }).start();
    }
}
