package com.atdit.booking.frontend.customer_registration.controllers;

import com.atdit.booking.backend.Ressources;
import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.financialdata.processing.FinancialInformationEvaluator;
import com.atdit.booking.frontend.super_controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the initial payment process page.
 * This class handles the start of the payment process and extends the base Controller.
 */
public class Page1PaymentProcessStartController extends Controller implements Initializable {



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Ressources.currentCustomer = new Customer();
        Ressources.financialInformation = Ressources.currentCustomer.getFinancialInformation();
        Ressources.financialInformationEvaluator = new FinancialInformationEvaluator(Ressources.currentCustomer.getFinancialInformation());

    }

    /**
     * Initiates the payment process by loading the next scene.
     * This method is triggered by a mouse event and navigates to the privacy policy page.
     *
     * @param e The MouseEvent that triggered this action, typically a button click
     */
    @FXML
    @SuppressWarnings("unused")
    public void start_registration(MouseEvent e) {
        loadScene(e,"customer_registration/page_2_data_privacy_form.fxml", "Datenschutzerklärung");
    }

}