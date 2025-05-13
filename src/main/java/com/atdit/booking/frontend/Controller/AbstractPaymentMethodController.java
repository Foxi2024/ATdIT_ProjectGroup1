package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.contracts.Contract;
import com.atdit.booking.backend.financialdata.financial_information.BankTransferDetails;
import com.atdit.booking.backend.financialdata.processing.PaymentMethodEvaluator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractPaymentMethodController extends Controller implements Initializable, Navigatable, Cacheable {

    /** Selected payment type from previous page */
    public static String selectedPayment;
    /** Contract object containing payment information */
    public static Contract contract;
    /** Evaluator for payment method validation */
    public static PaymentMethodEvaluator evaluator;

    protected String paymentMethod;
    protected String otherPage;

    @FXML protected Button backButton;
    @FXML protected ComboBox<String> paymentMethodCombo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedPayment = Page8aSelectPaymentController.selectedPayment;
        contract = Page8aSelectPaymentController.contract;
        evaluator = new PaymentMethodEvaluator();

        initializeParameters();

        if(selectedPayment.equals("One-Time")){
            backButton.setDisable(true);
        }

        paymentMethodCombo.getItems().addAll(
                "Kreditkarte",
                "Überweisung"
        );

        paymentMethodCombo.setValue(paymentMethod);
        contract.setPaymentMethod(paymentMethod);

        restoreData();
    }

    protected abstract void validateInfo();
    protected abstract void initializeParameters();

    @FXML
    public void selectPaymentMethod(ActionEvent e) {

        String selected = paymentMethodCombo.getValue();

        if (!selected.equals(paymentMethod)) {

            loadScene(e, otherPage, "Zahlungsmethode auswählen");
        }
    }

    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "payment_selection_page.fxml", "Zahlungsart auswählen");
    }

    @FXML
    public void nextPage(MouseEvent e) {

        cacheData();

        try {
            validateInfo();
        }
        catch (ValidationException ex) {
            showError("Validierungsfehler", "Fehler beim validieren Ihrer angegebenen Daten", ex.getMessage());
            return;
        }

        switch (selectedPayment) {
            case "BuyNowPayLater" -> loadScene(e, "Buy_now_pay_later_contract_page.fxml", "Vertragsdetails");
            case "Financing" -> loadScene(e, "financing_contract_page.fxml", "Vertragsdetails");
            case "One-Time" -> loadScene(e, "one_time_payment_contract_page.fxml", "Vertragsdetails");
        }
    }

}
