package com.atdit.booking.frontend.customer_registration;

import com.atdit.booking.Main;
import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.customer.CustomerEvaluator;
import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.contracts.OneTimePaymentContract;
import com.atdit.booking.frontend.super_controller.*;
import com.atdit.booking.frontend.payment_process.Page1ControllerPageLogin;
import com.atdit.booking.frontend.payment_process.Page2aSelectPaymentController;
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
/**
 * Controller class for handling personal information input on page 3 of the booking application.
 * This class manages the collection and validation of customer details including:
 * - Title (Mr/Mrs)
 * - First and last name
 * - Birth date
 * - Country and address
 * - Email address
 *
 * The controller provides functionality for:
 * - Initializing form fields
 * - Data validation before proceeding
 * - Navigation between pages
 * - Caching and restoring form data
 */
public class Page3PersonalInformationController extends Controller implements Initializable, Navigatable, Cacheable {

    /** ComboBox for selecting the customer's title (Mr/Mrs) */
    @FXML private ComboBox<String> titleField;
    /** TextField for the customer's last name */
    @FXML private TextField nameField;
    /** TextField for the customer's first name */
    @FXML private TextField firstNameField;
    /** DatePicker for selecting the customer's birth date */
    @FXML private DatePicker birthDatePicker;
    /** TextField for the customer's country */
    @FXML private TextField countryField;
    /** TextField for the customer's address */
    @FXML private TextField addressField;
    /** TextField for the customer's email address */
    @FXML private TextField emailField;
    /** Button to proceed to the next page */
    @FXML private Button continueButton;
    /** Button to return to the previous page */
    @FXML private Button backButton;
    /** Button to return to the previous page */
    @FXML private Button oneTimePaymentButton;

    /** Reference to the current customer object */
    private Customer currentCustomer ;
    /** Customer validator instance */
    private CustomerEvaluator evaluator;

    /**
     * Initializes the controller after FXML loading.
     * Sets up the current customer, evaluator, and UI components.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.currentCustomer = Main.customer;
        this.evaluator = new CustomerEvaluator(currentCustomer);

        restoreData();

        titleField.setItems(FXCollections.observableArrayList("Herr", "Frau"));
        titleField.setValue("Herr");
    }

    /**
     * Handles navigation to the next page.
     * Validates customer information before proceeding.
     *
     * @param e The MouseEvent that triggered the action
     */
    @FXML
    @SuppressWarnings("unused")
    public void nextPage(MouseEvent e) {
        try {
            evaluator.evaluateCustomerInfo();
        }
        catch (ValidationException ex) {
            showError("Validierung fehlgeschlagen", "Die Validierung Ihrer persönlichen Daten ist fehlgeschlagen.", ex.getMessage());
            return;
        }

        loadScene(e, "customer_registration/page_4_financial_info_declaration.fxml", "Finanzielle Angaben");
    }

    /**
     * Handles navigation to the previous page.
     *
     * @param e The MouseEvent that triggered the action
     */
    @FXML
    @SuppressWarnings("unused")
    public void previousPage(MouseEvent e) {
        loadScene(e, "customer_registration/page_2_data_privacy_form.fxml", "Datenschutzerklärung");
    }

    @FXML
    @SuppressWarnings("unused")
    public void goToOneTimePayment(MouseEvent e){

        cacheData();

        try {
            evaluator.evaluateCustomerInfo();
        }
        catch (ValidationException ex) {
            showConfirmation("Validierung fehlgeschlagen", "Validierung Ihrer persönlichen Daten ist fehlgeschlagen.", ex.getMessage());
            return;
        }

        Page2aSelectPaymentController.contract = new OneTimePaymentContract();
        Page2aSelectPaymentController.selectedPayment = "One-Time";
        Page1ControllerPageLogin.currentCustomer = currentCustomer;
        loadScene(e, "payment_process/page_3a_creditcard.fxml", "Zahlungsmethode auswählen");
    }

    /**
     * Saves the current form data to the customer object.
     * Implements the Cacheable interface.
     */
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

    /**
     * Restores previously saved customer data to the form fields.
     * Implements the Cacheable interface.
     */
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