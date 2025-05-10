package com.atdit.booking.Controller;


import com.atdit.booking.customer.Customer;
import com.atdit.booking.customer.CustomerDatabase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Page7ControllerPageLogin extends Controller implements Initializable {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    private CustomerDatabase db;
    public static Customer currentCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            db = new CustomerDatabase();
        }
        catch (SQLException e) {
            showError("Database Error", "Could not connect to database", e.getMessage());
        }
    }

    @FXML
    public void handleLogin(MouseEvent e) {

        String email = "fsj.raffel@gmail.com";
        String password = "Felix911.";

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both email and password");
            return;
        }

        try {

            currentCustomer = db.getCustomerWithFinancialInfoByEmail(email, password);
            System.out.println(currentCustomer);

            if (currentCustomer == null) {
                errorLabel.setText("Wrong email or password");
                return;
            }

            loadScene(e, "payment_selection_page.fxml", "Payment Selection");

        } catch (SQLException ex) {
            showError("Login Error", "Could not verify credentials", ex.getMessage());

        } catch (RuntimeException ex) {

            errorLabel.setText("Invalid email or password "+ ex.getMessage());
        }
    }

    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "page_6.fxml", "Account Creation");
    }
}