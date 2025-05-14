package com.atdit.booking.frontend.customer_registration.controllers;

import com.atdit.booking.CustomerRegistrationApplication;
import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.frontend.super_controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the account confirmation view.
 * This controller handles the confirmation screen that appears after account creation,
 * displaying a message that confirms an email will be sent to the user.
 */
public class Page7AccountConfirmationController extends Controller implements Initializable {

    /**
     * Label element that displays the email confirmation message.
     */
    @FXML
    private Label emailLabel;

    /**
     * Static reference to the current customer, initialized from the main application.
     */
    public static Customer currentCustomer = CustomerRegistrationApplication.customer;

    /**
     * Initializes the controller and sets up the confirmation message.
     * This method is called automatically after the FXML file has been loaded.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailLabel.setText("Eine Bestätigung wird an " + currentCustomer.getEmail() + " gesendet.");
    }

    /**
     * Closes the application window when triggered by a mouse event.
     * This method is called when the user clicks on a designated close button or area.
     *
     * @param event The MouseEvent that triggered this action.
     */
    @FXML
    @SuppressWarnings("unused")
    public void closeApplication(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}