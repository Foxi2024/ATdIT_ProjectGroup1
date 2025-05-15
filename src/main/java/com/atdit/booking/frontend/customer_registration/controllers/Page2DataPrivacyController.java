package com.atdit.booking.frontend.customer_registration.controllers;

import com.atdit.booking.backend.Resources;
import com.atdit.booking.frontend.abstract_controller.Controller;
import com.atdit.booking.frontend.abstract_controller.Navigatable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the data privacy page (page 2) of the booking application.
 * This class handles the display and interaction with the data privacy agreement.
 * Implements Initializable for JavaFX initialization and Navigatable for navigation functionality.
 */
public class Page2DataPrivacyController extends Controller implements Initializable, Navigatable {

    /** Radio button for accepting the data privacy agreement */
    @FXML private RadioButton acceptButton;

    /** Button to continue to the next page */
    @FXML private Button continueButton;

    /** Text area containing the data privacy agreement */
    @FXML private TextArea contractTextArea;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up the data privacy agreement text in the text area.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        contractTextArea.setText(Resources.DATA_PRIVACY_TEXT);
    }

    /**
     * Handles navigation to the next page when the continue button is clicked.
     * Only proceeds if the user has accepted the data privacy agreement.
     *
     * @param e The mouse event that triggered this method
     */
    @FXML
    @SuppressWarnings("unused")
    public void nextPage(MouseEvent e) {
        if (!acceptButton.isSelected()) {
            showError("Fehler", "Datenschutzerklärung nicht akzeptiert", "Bitte akzeptieren Sie die Datenschutzerklärung um fortzufahren.");
            return;
        }

        loadScene(e, "customer_registration/page_3_personal_information.fxml", "Persönliche Informationen");
    }

    /**
     * Handles navigation to the previous page.
     *
     * @param e The mouse event that triggered this method
     */
    @FXML
    @SuppressWarnings("unused")
    public void previousPage(MouseEvent e) {
        loadScene(e, "customer_registration/page_1_start_customer_registration.fxml", "Start");
    }

    /**
     * Updates the state of the continue button based on whether the user has accepted the agreement.
     * The continue button is enabled only when the agreement is accepted.
     *
     * @param e The mouse event that triggered this method
     */
    @FXML
    @SuppressWarnings("unused")
    public void acceptAgreement(MouseEvent e) {
        continueButton.setDisable(!acceptButton.isSelected());
    }
}