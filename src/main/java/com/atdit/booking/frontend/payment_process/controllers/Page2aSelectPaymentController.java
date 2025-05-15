package com.atdit.booking.frontend.payment_process.controllers;

import com.atdit.booking.backend.Ressources;
import com.atdit.booking.backend.financialdata.contracts.Contract;
import com.atdit.booking.backend.financialdata.contracts.FinancingContract;
import com.atdit.booking.backend.financialdata.contracts.BuyNowPayLaterContract;
import com.atdit.booking.frontend.super_controller.Controller;
import com.atdit.booking.frontend.super_controller.Navigatable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the payment selection page.
 * Handles the selection between one-time payment and financing options.
 * Implements Initializable for JavaFX initialization and Navigatable for scene navigation.
 */
public class Page2aSelectPaymentController extends Controller implements Initializable, Navigatable {
    /** Radio button for one-time payment selection */
    @FXML private RadioButton oneTimePaymentRadio;

    /** Radio button for financing selection */
    @FXML private RadioButton financingRadio;

    /**
     * Initializes the controller.
     * This method is called automatically after the FXML file has been loaded.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Handles the navigation to the next page based on the selected payment method.
     * Creates appropriate contract type and loads corresponding scene.
     *
     * @param e The mouse event that triggered this method
     */
    @FXML
    @SuppressWarnings("unused")
    public void nextPage(MouseEvent e) {
        if (oneTimePaymentRadio.isSelected()) {
            Ressources.selectedPayment = "BuyNowPayLater";
            Ressources.contract = new BuyNowPayLaterContract();
            loadScene(e, "payment_process/page_3a_creditcard.fxml", "Zahlungsmethode auswählen");
        } else {
            Ressources.selectedPayment = "Financing";
            Ressources.contract = new FinancingContract();
            loadScene(e, "payment_process/page_2b_installment_selection.fxml", "Finanzierung auswählen");
        }
    }

    /**
     * Handles navigation to the previous page (customer login).
     *
     * @param e The mouse event that triggered this method
     */
    @FXML
    @SuppressWarnings("unused")
    public void previousPage(MouseEvent e) {
        loadScene(e, "payment_process/page_1_customer_login.fxml", "Anmeldung");
    }

}