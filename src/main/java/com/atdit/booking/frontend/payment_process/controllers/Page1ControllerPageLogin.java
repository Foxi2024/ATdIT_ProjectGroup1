package com.atdit.booking.frontend.payment_process.controllers;

import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.database.DatabaseService;
import com.atdit.booking.backend.exceptions.CryptographyException;
import com.atdit.booking.frontend.super_controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller class for the login page of the application.
 * This class handles user authentication and database interactions for the login process.
 */
public class Page1ControllerPageLogin extends Controller implements Initializable {

    /** TextField for user email input */
    @FXML private TextField emailField;

    /** PasswordField for user password input */
    @FXML private PasswordField passwordField;

    /** Label to display error messages */
    @FXML private Label errorLabel;

    /** Button to trigger the login process */
    @FXML private Button loginButton;

    /** Database service instance for database operations */
    private DatabaseService db;

    /** Static reference to store the currently logged-in customer */
    public static Customer currentCustomer;

    /**
     * Initializes the controller class.
     * Sets up the database connection when the page is loaded.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            db = new DatabaseService();
        }
        catch (SQLException e) {
            showError("Datenbankfehler", "Verbindung zur Datenbank ist fehlgeschlagen.", e.getMessage());
        }
    }

    /**
     * Handles the login button click event.
     * Authenticates the user credentials and loads the payment selection page upon successful login.
     *
     * @param e The MouseEvent triggered by clicking the login button
     */
    @FXML
    @SuppressWarnings("unused")
    public void handleLogin(MouseEvent e) {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            currentCustomer = db.getCustomerWithFinancialInfoByEmail(email, password);
        }
        catch (SQLException | CryptographyException ex) {
            showError("Anmeldefehler", "Anmeldedaten konnten nicht überprüft werden.", ex.getMessage());
            return;
        } catch (IllegalArgumentException ex) {
            showError("Anmeldefehler", "Anmeldung konnte nicht durchgeführt werden", ex.getMessage());
            return;
        }

        loadScene(e, "payment_process/page_2a_payment_selection.fxml", "Zahlungsart auswählen");
    }
}