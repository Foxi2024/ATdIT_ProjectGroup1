package com.atdit.booking.frontend.Controller;

import javafx.scene.input.MouseEvent;

/**
 * Controller class for the initial payment process page.
 * This class handles the start of the payment process and extends the base Controller.
 */
public class Page1PaymentProcessStartController extends Controller {

    /**
     * Initiates the payment process by loading the next scene.
     * This method is triggered by a mouse event and navigates to the privacy policy page.
     *
     * @param e The MouseEvent that triggered this action, typically a button click
     */
    @FXML
    @SuppressWarnings("unused")
    public void start_payment_process(MouseEvent e) {
        loadScene(e,"page_2_data_privacy_form.fxml", "Datenschutzerklärung");
    }

}