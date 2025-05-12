package com.atdit.booking.frontend.Controller;


import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.database.DatabaseService;
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

public class Page7ControllerPageLogin extends Controller implements Initializable {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    private DatabaseService db;
    public static Customer currentCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            db = new DatabaseService();
        }
        catch (SQLException e) {
            showError("Datenbankfehler", "Verbindung zur Datenbank ist fehlgeschlagen.", e.getMessage());
        }
    }

    @FXML
    public void handleLogin(MouseEvent e) {

        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Bitte geben Sie Email und Passwort ein");
            return;
        }

        try {

            currentCustomer = db.getCustomerWithFinancialInfoByEmail(email, password);

            if (currentCustomer == null) {
                errorLabel.setText("Falsche Email oder Passwort");
                return;
            }

            loadScene(e, "payment_selection_page.fxml", "Zahlungsart auswählen");

        } catch (SQLException ex) {
            showError("Anmeldefehler", "Anmeldedaten konnten nicht überprüft werden.", ex.getMessage());

        } catch (RuntimeException ex) {

            errorLabel.setText("E-Mail oder Password ist falsch"+ ex.getMessage());
        }
    }

}