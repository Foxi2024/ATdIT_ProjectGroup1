package com.atdit.booking.Controller;

import com.atdit.booking.Main;
import com.atdit.booking.customer.Customer;
import com.atdit.booking.customer.CustomerEvaluater;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPage3 extends Controller implements Initializable {

    @FXML private TextField titleField;
    @FXML private TextField nameField;
    @FXML private TextField firstNameField;
    @FXML private DatePicker birthDatePicker;
    @FXML private TextField countryField;
    @FXML private TextField addressField;
    @FXML private TextField emailField;
    @FXML private Button continueButton;
    @FXML private Button backButton;

    private final Customer currentCustomer = Main.customer;
    private final CustomerEvaluater evaluater = new CustomerEvaluater(currentCustomer);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        restoreFormData();
    }

    @FXML
    public void nextPage(MouseEvent e) {

        cacheData();
        loadScene(e, "page_4.fxml", "Financial Information");
    }

    @FXML
    public void previousPage(MouseEvent e) {

        cacheData();
        loadScene(e, "page_2.fxml", "Terms and Conditions");
    }

    private void cacheData() throws IllegalArgumentException {

        currentCustomer.setTitle(titleField.getText());
        currentCustomer.setName(nameField.getText());
        currentCustomer.setFirstName(firstNameField.getText());
        currentCustomer.setCountry(countryField.getText());
        currentCustomer.setAddress(addressField.getText());
        currentCustomer.setEmail(emailField.getText());

        if (birthDatePicker.getValue() != null) {
            currentCustomer.setBirthdate(birthDatePicker.getValue().toString());
        } else {
            currentCustomer.setBirthdate("");
        }

    }

    private void restoreFormData() {

        titleField.setText(currentCustomer.getTitle());
        nameField.setText(currentCustomer.getName());
        firstNameField.setText(currentCustomer.getFirstName());
        countryField.setText(currentCustomer.getCountry());
        addressField.setText(currentCustomer.getAddress());
        emailField.setText(Main.customer.getEmail());

        if (currentCustomer.getBirthdate() != null && !currentCustomer.getBirthdate().isEmpty()) {
            birthDatePicker.setValue(java.time.LocalDate.parse(currentCustomer.getBirthdate()));
        }
    }

}