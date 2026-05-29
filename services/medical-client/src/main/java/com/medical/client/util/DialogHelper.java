package com.medical.client.util;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Utility class for building styled JavaFX dialog forms
 * with a consistent dark theme matching the application.
 */
public class DialogHelper {

    /**
     * Creates a pre-configured dialog with dark theme styling.
     */
    public static <R> Dialog<R> createStyledDialog(String title) {
        Dialog<R> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(null);

        DialogPane pane = dialog.getDialogPane();
        pane.getStylesheets().add(DialogHelper.class.getResource("/css/styles.css").toExternalForm());
        pane.getStyleClass().add("dialog-pane");

        return dialog;
    }

    /**
     * Creates a pre-configured grid for form layouts inside dialogs.
     */
    public static GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
        return grid;
    }

    /**
     * Adds a labeled TextField to the grid at the specified row.
     * Returns the TextField for binding.
     */
    public static TextField addTextField(GridPane grid, int row, String labelText, String promptText) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: #e9ecef;");
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-border-color: #0f3460; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 6;");
        GridPane.setHgrow(field, Priority.ALWAYS);
        field.setPrefWidth(250);
        grid.add(label, 0, row);
        grid.add(field, 1, row);
        return field;
    }

    /**
     * Adds a labeled TextField pre-filled with a value.
     */
    public static TextField addTextField(GridPane grid, int row, String labelText, String promptText, String value) {
        TextField field = addTextField(grid, row, labelText, promptText);
        if (value != null) {
            field.setText(value);
        }
        return field;
    }

    /**
     * Adds a labeled PasswordField to the grid at the specified row.
     */
    public static PasswordField addPasswordField(GridPane grid, int row, String labelText, String promptText) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: #e9ecef;");
        PasswordField field = new PasswordField();
        field.setPromptText(promptText);
        field.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-border-color: #0f3460; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 6;");
        GridPane.setHgrow(field, Priority.ALWAYS);
        field.setPrefWidth(250);
        grid.add(label, 0, row);
        grid.add(field, 1, row);
        return field;
    }

    /**
     * Adds a labeled ComboBox to the grid at the specified row.
     */
    @SuppressWarnings("unchecked")
    public static <T> ComboBox<T> addComboBox(GridPane grid, int row, String labelText, T... items) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: #e9ecef;");
        ComboBox<T> combo = new ComboBox<>();
        combo.getItems().addAll(items);
        combo.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-border-color: #0f3460; -fx-border-radius: 4; -fx-background-radius: 4;");
        combo.setPrefWidth(250);
        grid.add(label, 0, row);
        grid.add(combo, 1, row);
        return combo;
    }

    /**
     * Adds a labeled DatePicker to the grid at the specified row.
     */
    public static DatePicker addDatePicker(GridPane grid, int row, String labelText) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: #e9ecef;");
        DatePicker picker = new DatePicker();
        picker.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-border-color: #0f3460; -fx-border-radius: 4; -fx-background-radius: 4;");
        picker.setPrefWidth(250);
        grid.add(label, 0, row);
        grid.add(picker, 1, row);
        return picker;
    }

    /**
     * Adds a labeled DatePicker with a pre-set value.
     */
    public static DatePicker addDatePicker(GridPane grid, int row, String labelText, LocalDate value) {
        DatePicker picker = addDatePicker(grid, row, labelText);
        picker.setValue(value);
        return picker;
    }

    /**
     * Adds a labeled TextArea to the grid at the specified row.
     */
    public static TextArea addTextArea(GridPane grid, int row, String labelText, String promptText) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: #e9ecef;");
        TextArea area = new TextArea();
        area.setPromptText(promptText);
        area.setPrefRowCount(3);
        area.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-border-color: #0f3460; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 6;");
        area.setWrapText(true);
        GridPane.setHgrow(area, Priority.ALWAYS);
        area.setPrefWidth(250);
        grid.add(label, 0, row);
        grid.add(area, 1, row);
        return area;
    }

    /**
     * Adds a labeled TextArea pre-filled with a value.
     */
    public static TextArea addTextArea(GridPane grid, int row, String labelText, String promptText, String value) {
        TextArea area = addTextArea(grid, row, labelText, promptText);
        if (value != null) {
            area.setText(value);
        }
        return area;
    }

    /**
     * Gets a non-blank string or returns null (for @JsonInclude NON_NULL serialization).
     */
    public static String getOrNull(String value) {
        return (value != null && !value.isBlank()) ? value.trim() : null;
    }
}
