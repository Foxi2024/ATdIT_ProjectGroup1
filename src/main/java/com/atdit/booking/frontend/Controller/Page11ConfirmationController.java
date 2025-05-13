package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.customer.Customer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the confirmation page (Page 11) of the booking application.
 * This class handles the display of booking confirmation details to the customer.
 */
public class Page11ConfirmationController implements Initializable {

    /** Label to display the confirmation message */
    @FXML private Label confirmationText;
    /** Label to display the delivery address */
    @FXML private Label addressText;
    /** Label to display the email address */
    @FXML private Label emailText;

    /** Reference to the current customer, obtained from the login page */
    private static final Customer currentCustomer = Page7ControllerPageLogin.currentCustomer;

    /**
     * Initializes the confirmation page by setting up the confirmation message,
     * delivery address and email confirmation text.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        confirmationText.setText("Ihre Buchung wurde erfolgreich abgeschlossen! Wir danken Ihnen für Ihr Vertrauen.");
        addressText.setText("Ihre Tickets werden gesendet an: " + currentCustomer.getAddress());
        emailText.setText("Eine Bestätigungsmail wurde gesendet an: " + currentCustomer.getEmail());
    }

    /**
     * Handles the application closing event.
     * When triggered, this method will terminate the application.
     *
     * @param event The mouse event that triggered the closing action
     */
    @FXML
    public void closeApplication(MouseEvent event) {
        Platform.exit();
    }
}