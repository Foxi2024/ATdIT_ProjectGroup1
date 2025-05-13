package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.contracts.Contract;
import com.atdit.booking.backend.financialdata.financial_information.CreditCardDetails;
import com.atdit.booking.backend.financialdata.financial_information.PaymentMethodEvaluator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the credit card payment page.
 * Handles user input for credit card details and navigation between payment pages.
 * Implements Initializable for JavaFX initialization, Navigatable for scene navigation,
 * and Cacheable for data persistence.
 */
public class Page9aCreditCardController extends Controller implements Initializable, Navigatable, Cacheable {

    /** ComboBox for selecting the payment method */
    @FXML public ComboBox<String> paymentMethodCombo;
    /** TextField for entering the credit card number */
    @FXML public TextField cardNumberField;
    /** TextField for entering the expiry date */
    @FXML private TextField expiryField;
    /** TextField for entering the CVV */
    @FXML private TextField cvvField;
    /** Container for expiry date field */
    @FXML private VBox expiryBox;
    /** Container for CVV field */
    @FXML private VBox cvvBox;
    /** Label for displaying card information */
    @FXML private Label cardLabel;
    /** Selected payment method from previous page */
    public static String selectedPayment = Page8aSelectPaymentController.selectedPayment;
    /** Contract instance from previous page */
    public static Contract contract = Page8aSelectPaymentController.contract;
    /** Credit card details instance */
    public static CreditCardDetails creditCardDetails = new CreditCardDetails();
    /** Payment method evaluator for validation */
    public static PaymentMethodEvaluator evaluator;

    /**
     * Initializes the controller after FXML injection.
     * Sets up the payment method combo box and restores any cached data.
     *
     * @param url The location used to resolve relative paths
     * @param resourceBundle The resources used by this controller
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        evaluator = new PaymentMethodEvaluator();
        evaluator.setCreditCardDetails(creditCardDetails);

        paymentMethodCombo.getItems().addAll(
                "Kreditkarte",
                "Überweisung"
        );

        paymentMethodCombo.setValue("Kreditkarte");
        contract.setPaymentMethod("Kreditkarte");

        restoreData();
    }

    /**
     * Handles payment method selection changes.
     * Navigates to bank transfer page if bank transfer is selected.
     *
     * @param e The action event triggered by the combo box selection
     */
    @FXML
    public void selectPaymentMethod(ActionEvent e) {

        String selected = paymentMethodCombo.getValue();

        if (selected.equals("Überweisung")) {

            loadScene(e, "banktransfer.fxml", "Zahlungsmethode auswählen");
        }
    }

    /**
     * Caches the current credit card details entered by the user.
     * Stores card number, expiry date, and CVV in the creditCardDetails object.
     */
    public void cacheData() {

        creditCardDetails.setCardNumber(cardNumberField.getText());
        creditCardDetails.setExpiryDate(expiryField.getText());
        creditCardDetails.setCvv(cvvField.getText());

    }

    /**
     * Restores previously cached credit card details to the form fields.
     */
    public void restoreData() {

        cardNumberField.setText(creditCardDetails.getCardNumber());
        expiryField.setText(creditCardDetails.getExpiryDate());
        cvvField.setText(creditCardDetails.getCvv());

    }

    /**
     * Navigates to the previous page (payment selection).
     *
     * @param e The mouse event that triggered the navigation
     */
    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "payment_selection_page.fxml", "Zahlungsart auswählen");
    }

    /**
     * Validates credit card details and navigates to the appropriate contract page.
     * Shows error message if validation fails.
     *
     * @param e The mouse event that triggered the navigation
     */
    @FXML
    public void nextPage(MouseEvent e) {

        cacheData();

        try {
            evaluator.validateCreditCardInfo();
        }
        catch (ValidationException ex) {
            showError("Validierungsfehler", "Fehler beim validieren Ihrer angegebenen Daten", ex.getMessage());
            return;
        }

        switch (selectedPayment) {
            case "One-Time" -> loadScene(e, "one_time_payment_contract_page.fxml", "Vertragsdetails");
            case "Financing" -> loadScene(e, "financing_contract_page.fxml", "Vertragsdetails");
        }
    }

}