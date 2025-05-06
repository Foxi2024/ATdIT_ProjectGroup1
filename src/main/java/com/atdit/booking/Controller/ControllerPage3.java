package com.atdit.booking.Controller;


import com.atdit.booking.Excpetions.EvaluationFailedError;
import com.atdit.booking.Main;
import com.atdit.booking.customer.Customer;
import com.atdit.booking.customer.CustomerEvaluater;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPage3 extends Controller implements Initializable {

    @FXML private ComboBox<String> titleField;
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
        titleField.setItems(FXCollections.observableArrayList("Mr", "Ms"));
        titleField.setValue("Mr");
    }

    @FXML
    public void nextPage(MouseEvent e) {

        cacheData();

        try {
            evaluater.evaluateCustomerInfo();
        } catch (IllegalArgumentException ex) {
            EvaluationFailedError.showEvaluationPersonalInformationFailedError(ex.getMessage());
            return;
        }

        loadScene(e, "page_4.fxml", "Financial Information");
    }

    @FXML
    public void previousPage(MouseEvent e) {

        cacheData();
        loadScene(e, "page_2.fxml", "Terms and Conditions");
    }

    private void cacheData() throws IllegalArgumentException {

        currentCustomer.setTitle(titleField.getValue());
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

        titleField.setValue(currentCustomer.getTitle());
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