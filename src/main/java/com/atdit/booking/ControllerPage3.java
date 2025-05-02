package com.atdit.booking;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ControllerPage3 extends Controller implements Initializable {

    @FXML private TextField titleField;
    @FXML private TextField nameField;
    @FXML private TextField firstNameField;
    @FXML private TextField nationalityField;
    @FXML private DatePicker birthDatePicker;
    @FXML private TextField streetField;
    @FXML private TextField houseNumberField;
    @FXML private TextField postalCodeField;
    @FXML private TextField cityField;
    @FXML private TextField countryField;
    @FXML private TextField emailField;
    @FXML private Button continueButton;
    @FXML private Button backButton;

    private static Map<String, String> savedData = new HashMap<>();
    private static boolean hasData = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (hasData) {
            restoreFormData();
        }
    }

    private void saveFormData() {

        savedData.put("title", titleField.getText());
        savedData.put("name", nameField.getText());
        savedData.put("firstName", firstNameField.getText());
        savedData.put("nationality", nationalityField.getText());
        savedData.put("birthDate", birthDatePicker.getValue() != null ? birthDatePicker.getValue().toString() : "");
        savedData.put("street", streetField.getText());
        savedData.put("houseNumber", houseNumberField.getText());
        savedData.put("postalCode", postalCodeField.getText());
        savedData.put("city", cityField.getText());
        savedData.put("country", countryField.getText());
        savedData.put("email", emailField.getText());
        hasData = true;
    }

    private void restoreFormData() {

        titleField.setText(savedData.get("title"));
        nameField.setText(savedData.get("name"));
        firstNameField.setText(savedData.get("firstName"));
        nationalityField.setText(savedData.get("nationality"));

        if (!savedData.get("birthDate").isEmpty()) {
            birthDatePicker.setValue(java.time.LocalDate.parse(savedData.get("birthDate")));
        }

        streetField.setText(savedData.get("street"));
        houseNumberField.setText(savedData.get("houseNumber"));
        postalCodeField.setText(savedData.get("postalCode"));
        cityField.setText(savedData.get("city"));
        countryField.setText(savedData.get("country"));
        emailField.setText(savedData.get("email"));

    }

    @FXML
    public void nextPage(MouseEvent e) {
        if (validateForm()) {
            saveFormData();
            Stage stage = (Stage) continueButton.getScene().getWindow();
            Scene scene = getScene("page_4.fxml");
            stage.setTitle("Financial Information");
            stage.setScene(scene);
        }
    }

    @FXML
    public void previousPage(MouseEvent e) {
        saveFormData();
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = getScene("page_2.fxml");
        stage.setTitle("Terms and Conditions");
        stage.setScene(scene);
    }

    private boolean validateForm() {
        StringBuilder errorMessage = new StringBuilder("Please fill out the following fields:\n");
        boolean isValid = true;

        if (nameField.getText().trim().isEmpty()) {
            errorMessage.append("- Name\n");
            isValid = false;
        }
        if (firstNameField.getText().trim().isEmpty()) {
            errorMessage.append("- First Name\n");
            isValid = false;
        }
        if (nationalityField.getText().trim().isEmpty()) {
            errorMessage.append("- Nationality\n");
            isValid = false;
        }
        if (birthDatePicker.getValue() == null) {
            errorMessage.append("- Birth Date\n");
            isValid = false;
        }
        if (streetField.getText().trim().isEmpty()) {
            errorMessage.append("- Street\n");
            isValid = false;
        }
        if (cityField.getText().trim().isEmpty()) {
            errorMessage.append("- City\n");
            isValid = false;
        }
        if (countryField.getText().trim().isEmpty()) {
            errorMessage.append("- Country\n");
            isValid = false;
        }
        if (emailField.getText().trim().isEmpty()) {
            errorMessage.append("- Email\n");
            isValid = false;
        }

        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText("Required Fields Empty");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }

        return isValid;
    }
}