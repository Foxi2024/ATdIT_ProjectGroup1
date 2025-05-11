package com.atdit.booking.Controller;

import com.atdit.booking.customer.Customer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Page11ConfirmationController implements Initializable {

    @FXML private Label confirmationText;
    @FXML private Label addressText;
    @FXML private Label emailText;

    private static final Customer currentCustomer = Page7ControllerPageLogin.currentCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        confirmationText.setText("Your booking has been successfully completed! We greatly appreciate your trust in our service.");
        addressText.setText("Your tickets will be sent to: " + currentCustomer.getAddress());
        emailText.setText("A confirmation email has been sent to: " + currentCustomer.getEmail());
    }

    @FXML
    public void closeApplication(MouseEvent event) {
        Platform.exit();
    }
}