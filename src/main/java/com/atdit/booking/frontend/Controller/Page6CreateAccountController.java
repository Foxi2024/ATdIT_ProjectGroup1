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

public class Page6CreateAccountController extends Controller implements Initializable {


    @FXML private Label emailLabel;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button createAccountButton;


    private static final Customer currentCustomer = Main.customer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        emailLabel.setText(currentCustomer.getEmail());

        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        try {
            DatabaseService db = new DatabaseService();
            passwordField.textProperty().addListener((obs, old, newValue) -> db.validatePasswords(password, confirm));
            confirmPasswordField.textProperty().addListener((obs, old, newValue) -> {
                try{
                    db.validatePasswords(password, confirm);
                }
                catch(ValidationException ex){
                    createAccountButton.setDisable(true);
                }

            });
        } catch (SQLException ex) {
            showError("Datenbankfehler", "Ein Fehler ist beim Speichern ihrer Daten aufgetreten.", ex.getMessage());
        }


    }

    @FXML
    public void previousPage(MouseEvent e) {

        loadScene(e, "page_5.fxml", "");
    }

    @FXML
    public void handleCreateAccount(MouseEvent e) {

        String password = passwordField.getText();

        try {
            DatabaseService db = new DatabaseService();
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