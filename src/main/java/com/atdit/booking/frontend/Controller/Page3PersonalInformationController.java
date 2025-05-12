package com.atdit.booking.frontend.Controller;

import com.atdit.booking.Main;
import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.customer.CustomerEvaluator;
import com.atdit.booking.backend.exceptions.ValidationException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Page3PersonalInformationController extends Controller implements Initializable, Navigatable, Cacheable {

    @FXML private ComboBox<String> titleField;
    @FXML private TextField nameField;
    @FXML private TextField firstNameField;
    @FXML private DatePicker birthDatePicker;
    @FXML private TextField countryField;
    @FXML private TextField addressField;
    @FXML private TextField emailField;
    @FXML private Button continueButton;
    @FXML private Button backButton;


    private Customer currentCustomer ;
    private CustomerEvaluator evaluator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.currentCustomer = Main.customer;
        this.evaluator = new CustomerEvaluator(currentCustomer);

        restoreData();

        titleField.setItems(FXCollections.observableArrayList("Herr", "Frau"));
        titleField.setValue("Herr");
    }

    @FXML
    public void nextPage(MouseEvent e) {

        cacheData();

        try {
            evaluator.evaluateCustomerInfo();
        }
        catch (ValidationException ex) {
            showError("Validierung fehlgeschlagen", "Validierung Ihrer persönlichen Daten ist fehlgeschlagen.", ex.getMessage());
            return;
        }

        loadScene(e, "page_4.fxml", "Finanzielle Angaben");
    }

    @FXML
    public void previousPage(MouseEvent e) {

        cacheData();
        loadScene(e, "page_2.fxml", "Datenschutzerklärung");
    }

    public void cacheData() {

        currentCustomer.setTitle(titleField.getValue());
        currentCustomer.setName(nameField.getText());
        currentCustomer.setFirstName(firstNameField.getText());
        currentCustomer.setCountry(countryField.getText());
        currentCustomer.setAddress(addressField.getText());
        currentCustomer.setEmail(emailField.getText());

        if (birthDatePicker.getValue() != null) {
            currentCustomer.setBirthdate(birthDatePicker.getValue().toString());
        }
        else {
            currentCustomer.setBirthdate("");
        }
    }

    public void restoreData() {

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