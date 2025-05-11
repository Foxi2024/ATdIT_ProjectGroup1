package com.atdit.booking.Controller;


import com.atdit.booking.Main;
import com.atdit.booking.customer.Customer;
import com.atdit.booking.customer.CustomerDatabase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Page6CreateAccountController extends Controller implements Initializable {


    @FXML private Label emailLabel;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button createAccountButton;
    @FXML private ProcessStepBarController_1 processStepBarController;


    private static final Customer currentCustomer = Main.customer;


    /*


    Ihre Daten wurden erfolgreich vom System überprüft. Der nächste Schritt ist die Erstellung eines persönlichen Accounts, um den Finanzierungsprozess weiter fortzuführen.

    Warum jetzt die Registrierung erforderlich ist:
    Ab diesem Punkt werden Ihre Daten sicher in unserer Datenbank gespeichert und für die finale Überprüfung durch unser Accounting-Team vorbereitet. Erst nach der Validierung durch Accounting erhalten Sie vollen Zugriff auf Ihre Buchungsoptionen, wie zum Beispiel Ratenzahlung oder andere Zahlungspläne.

    Bitte registrieren Sie sich jetzt, um den nächsten Schritt in Ihrer Reiseplanung zu gehen.


     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //processStepBarController.setCurrentStep("account");

        emailLabel.setText(currentCustomer.getEmail());
        passwordField.textProperty().addListener((obs, old, newValue) -> validatePasswords());
        confirmPasswordField.textProperty().addListener((obs, old, newValue) -> validatePasswords());
    }

    private void validatePasswords() {

        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        boolean isValid = password.equals(confirm) &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?].*");

        createAccountButton.setDisable(!isValid);
    }

    @FXML
    public void previousPage(MouseEvent e) {

        loadScene(e, "page_5.fxml", "");
    }

    @FXML
    public void handleCreateAccount(MouseEvent e) {

        String password = passwordField.getText();

        try {
            CustomerDatabase db = new CustomerDatabase();
            db.setCurrentCustomer(currentCustomer);
            db.saveCustomerInDatabase(password);
        }
        catch (SQLException | RuntimeException ex) {
            showError("Datenbankfehler", "Ein Fehler ist beim Speichern ihrer Daten aufgetreten.", ex.getMessage());
            return;
        }

        loadScene(e, "customer_login.fxml", "Payment Selection");

    }
}