package com.atdit.booking.Controller;



import com.atdit.booking.Cacheable;
import com.atdit.booking.Main;
import com.atdit.booking.Navigatable;
import com.atdit.booking.customer.Customer;
import com.atdit.booking.customer.CustomerEvaluater;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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


    private final Customer currentCustomer = Main.customer;
    private final CustomerEvaluater evaluater = new CustomerEvaluater(currentCustomer);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        restoreData();

        titleField.setItems(FXCollections.observableArrayList("Herr", "Frau"));
        titleField.setValue("Herr");
    }

    @FXML
    public void nextPage(MouseEvent e) {

        cacheData();

        try {
            evaluater.evaluateCustomerInfo();
        } catch (IllegalArgumentException ex) {
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
        } else {
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