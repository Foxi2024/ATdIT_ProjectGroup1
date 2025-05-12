package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.customer.Customer;
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

        confirmationText.setText("Ihre Buchung wurde erfolgreich abgeschlossen! Wir danken Ihnen für Ihr Vertrauen.");
        addressText.setText("Ihre Tickets werden gesendet an: " + currentCustomer.getAddress());
        emailText.setText("Eine Bestätigungsmail wurde gesendet an: " + currentCustomer.getEmail());
    }

    @FXML
    public void closeApplication(MouseEvent event) {
        Platform.exit();
    }
}