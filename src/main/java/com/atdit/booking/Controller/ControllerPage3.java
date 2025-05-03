package com.atdit.booking.Controller;

import com.atdit.booking.Main;
import com.atdit.booking.customer.Customer;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { restoreFormData(); }

    @FXML
    public void nextPage(MouseEvent e) {

        if (validateForm()) {
            cacheData();
            Stage stage = (Stage) continueButton.getScene().getWindow();
            Scene scene = getScene("page_4.fxml");
            stage.setTitle("Financial Information");
            stage.setScene(scene);
        }
    }

    @FXML
    public void previousPage(MouseEvent e) {
        cacheData();
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = getScene("page_2.fxml");
        stage.setTitle("Terms and Conditions");
        stage.setScene(scene);
    }

    private void cacheData(){

        Customer currentCostumer = Main.customer;
        currentCostumer.setTitle(titleField.getText());
        currentCostumer.setFirstName(firstNameField.getText());
        currentCostumer.setName(nameField.getText());
        currentCostumer.setCountry(countryField.getText());
        currentCostumer.setPostalCode(postalCodeField.getText());
        currentCostumer.setCity(cityField.getText());
        currentCostumer.setStreetname(streetField.getText());
        currentCostumer.setHouseNumber(houseNumberField.getText());
        currentCostumer.setEmail(emailField.getText());

        if(birthDatePicker.getValue() == null){
            currentCostumer.setBirthdate(null);
        } else {
            currentCostumer.setBirthdate(birthDatePicker.getValue().toString());
        }
    }

    private void restoreFormData() {

        titleField.setText(Main.customer.getTitle());
        nameField.setText(Main.customer.getName());
        firstNameField.setText(Main.customer.getFirstName());
        nationalityField.setText(Main.customer.getCountry());
        streetField.setText(Main.customer.getStreetname());
        houseNumberField.setText(String.valueOf(Main.customer.getHouseNumber()));
        postalCodeField.setText(String.valueOf(Main.customer.getPostalCode()));
        cityField.setText(Main.customer.getCity());
        countryField.setText(Main.customer.getCountry());
        emailField.setText(Main.customer.getEmail());

        if (Main.customer.getBirthdate() != null && !Main.customer.getBirthdate().isEmpty()) {
            birthDatePicker.setValue(java.time.LocalDate.parse(Main.customer.getBirthdate()));
        }
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
            showError("Missing Information", "Please fill out all required fields", errorMessage.toString());
        }

        return isValid;
    }
}