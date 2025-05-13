package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.contracts.Contract;
import com.atdit.booking.backend.financialdata.financial_information.BankTransferDetails;
import com.atdit.booking.backend.financialdata.financial_information.PaymentMethodEvaluator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for handling bank transfer payment details in the application.
 * This class manages the bank transfer form interface and related data processing.
 * Implements Initializable for JavaFX initialization, Navigatable for scene navigation,
 * and Cacheable for data persistence.
 */
public class Page9bBankTransferController extends Controller implements Initializable, Navigatable, Cacheable {

    /** ComboBox for selecting the payment method */
    @FXML private ComboBox<String> paymentMethodCombo;
    /** TextField for entering IBAN */
    @FXML private TextField ibanField;
    /** TextField for entering account holder name */
    @FXML private TextField accountHolderField;
    /** TextField for entering bank name */
    @FXML private TextField bankNameField;
    /** TextField for entering BIC/SWIFT code */
    @FXML private TextField bicField;

    /** Selected payment type from previous page */
    public static String selectedPayment = Page8aSelectPaymentController.selectedPayment;
    /** Contract object containing payment information */
    public static Contract contract = Page8aSelectPaymentController.contract;
    /** Object storing bank transfer details */
    public static BankTransferDetails bankTransferDetails = new BankTransferDetails();
    /** Evaluator for payment method validation */
    public static PaymentMethodEvaluator evaluator;

    /**
     * Initializes the controller class. Sets up the payment method combo box,
     * initializes the evaluator, and restores any previously cached data.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        evaluator = new PaymentMethodEvaluator();
        evaluator.setBankTransferDetails(bankTransferDetails);

        paymentMethodCombo.getItems().addAll(
                "Kreditkarte",
                "Überweisung"
        );

        paymentMethodCombo.setValue("Überweisung");
        contract.setPaymentMethod("Überweisung");

        restoreData();
    }

    /**
     * Handles payment method selection changes.
     * If credit card is selected, redirects to the credit card form.
     *
     * @param e The action event triggered by selection change
     */
    @FXML
    public void selectPaymentMethod(ActionEvent e) {

        String selected = paymentMethodCombo.getValue();

        if (selected.equals("Kreditkarte")) {

            loadScene(e, "creditcard.fxml", "Zahlungsmethode auswählen");
        }
    }

    /**
     * Saves the current form data to the bankTransferDetails object.
     * Implements the Cacheable interface method.
     */
    public void cacheData() {

        bankTransferDetails.setIban(ibanField.getText());
        bankTransferDetails.setAccountHolder(accountHolderField.getText());
        bankTransferDetails.setBankName(bankNameField.getText());
        bankTransferDetails.setBicSwift(bicField.getText());
    }

    /**
     * Restores previously cached data to the form fields.
     * Implements the Cacheable interface method.
     */
    public void restoreData() {

        ibanField.setText(bankTransferDetails.getIban());
        accountHolderField.setText(bankTransferDetails.getAccountHolder());
        bankNameField.setText(bankTransferDetails.getBankName());
        bicField.setText(bankTransferDetails.getBicSwift());
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
     * Processes the form data and navigates to the next page.
     * Validates the bank transfer information before proceeding.
     * Routes to different contract pages based on the selected payment type.
     *
     * @param e The mouse event that triggered the navigation
     */
    @FXML
    public void nextPage(MouseEvent e) {

        cacheData();

        try {
            evaluator.validateBankTransferInfo();
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