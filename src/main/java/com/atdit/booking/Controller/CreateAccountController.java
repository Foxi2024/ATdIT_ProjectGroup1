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

public class CreateAccountController extends Controller implements Initializable {


    @FXML private Label emailLabel;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button createAccountButton;

    private static final Customer currentCustomer = Main.customer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

        loadScene(e, "page_5.fxml", "Document Upload");
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
            showError("Database Error", "There was an error while saving your data.", ex.getMessage());
            return;
        }

        loadScene(e, "customer_login.fxml", "Payment Selection");

    }
}