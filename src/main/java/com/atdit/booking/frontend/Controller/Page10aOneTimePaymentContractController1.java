package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.financialdata.contracts.BuyNowPayLaterContract;
import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.financialdata.contracts.OneTimePaymentContract;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for handling one-time payment contracts in the booking frontend.
 * This class manages the display and interaction with contract details, customer information,
 * and payment information.
 */
public class Page10aOneTimePaymentContractController1 extends Controller implements Initializable, Navigatable {

    /** FXML injected UI elements */
    @FXML private Label customerNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label paymentMethodLabel;
    @FXML private TextArea contractTextArea;
    @FXML private RadioButton signatureCheckbox;
    @FXML private Button continueButton;

    /** Fixed total amount for the contract */
    private static final int TOTAL_AMOUNT = 5000;
    /** Current contract instance from previous payment selection */
    public static OneTimePaymentContract contract = (OneTimePaymentContract) Page8aSelectPaymentController.contract;
    /** Current customer instance from login */
    public static Customer currentCustomer = Page7ControllerPageLogin.currentCustomer;

    /**
     * Initializes the controller after FXML loading.
     * Populates the UI elements with customer and contract information.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerNameLabel.setText(currentCustomer.getFirstName() + " " + currentCustomer.getName());
        emailLabel.setText(currentCustomer.getEmail());
        paymentMethodLabel.setText(contract.getPaymentMethod());
        totalAmountLabel.setText(String.format("€%.2f", (double) TOTAL_AMOUNT));
        contractTextArea.setText(contract.getContractText());
    }

    /**
     * Handles the contract signature checkbox event.
     * Enables/disables the continue button based on checkbox selection.
     *
     * @param e The mouse event that triggered this method
     */
    @FXML
    public void signContract(MouseEvent e) {

        if (signatureCheckbox.isSelected()) {
            continueButton.setDisable(false);
            return;
        }

        continueButton.setDisable(true);
    }

    /**
     * Navigates to the previous page based on the selected payment method.
     *
     * @param e The mouse event that triggered this method
     */
    @FXML
    public void previousPage(MouseEvent e) {

        if(contract.getPaymentMethod().equals("Kreditkarte")) {
            loadScene(e, "creditcard.fxml", "Zahlungsmethode auswählen");
        } else {
            loadScene(e, "banktransfer.fxml", "Zahlungsmethode auswählen");
        }
    }

    /**
     * Navigates to the confirmation page.
     *
     * @param e The mouse event that triggered this method
     */
    @FXML
    public void nextPage(MouseEvent e) {
        loadScene(e, "confirmation_page.fxml", "Confirmation");
    }
}