package com.atdit.booking.frontend.Controller;

import com.atdit.booking.Main;
import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.database.DatabaseService;
import com.atdit.booking.backend.exceptions.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller class for the account creation page (page 6).
 * Handles the password creation and account finalization process.
 * Extends the base Controller class and implements Initializable for JavaFX initialization.
 */
public class Page6CreateAccountController extends Controller implements Initializable {

    /** Label displaying the customer's email address */
    @FXML private Label emailLabel;

    /** Field for entering the password */
    @FXML private PasswordField passwordField;

    /** Field for confirming the password */
    @FXML private PasswordField confirmPasswordField;

    /** Button to create the account */
    @FXML private Button createAccountButton;

    /** Reference to the current customer being created */
    private static final Customer currentCustomer = Main.customer;

    /** Database service instance for handling database operations */
    private DatabaseService db;

    /**
     * Initializes the controller after FXML loading.
     * Sets up the email display and password field listeners.
     * Establishes database connection.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        emailLabel.setText(currentCustomer.getEmail());

        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        try {
            db = new DatabaseService();
        }
        catch (SQLException ex) {
            showError("Datenbankfehler", "Ein Fehler ist beim Speichern ihrer Daten aufgetreten.", ex.getMessage());
        }

        passwordField.textProperty().addListener((obs, old, newValue) -> toggleCreateButton(password, confirm));
        confirmPasswordField.textProperty().addListener((obs, old, newValue) -> toggleCreateButton(password, confirm));
    }

    /**
     * Toggles the create account button based on password validation.
     * Disables the button if passwords don't match validation criteria.
     *
     * @param password The entered password
     * @param confirm The confirmation password
     */
    private void toggleCreateButton(String password, String confirm) {

        try{
            db.validatePasswords(password, confirm);
        }
        catch(ValidationException ex){
            createAccountButton.setDisable(true);
        }
    }

    /**
     * Handles navigation to the previous page.
     *
     * @param e The mouse event that triggered the action
     */
    @FXML
    public void previousPage(MouseEvent e) {

        loadScene(e, "page_5.fxml", "");
    }

    /**
     * Handles the account creation process.
     * Saves the customer data to the database and navigates to the confirmation page.
     * Shows error message if database operation fails.
     *
     * @param e The mouse event that triggered the action
     */
    @FXML
    public void handleCreateAccount(MouseEvent e) {

        String password = passwordField.getText();

        try {
            db.setCurrentCustomer(currentCustomer);
            db.saveCustomerInDatabase(password);
        }
        catch (SQLException | RuntimeException ex) {
            showError("Datenbankfehler", "Ein Fehler ist beim Speichern ihrer Daten aufgetreten.", ex.getMessage());
            return;
        }

        loadScene(e, "account_created.fxml", "Vielen Dank");

    }
}