package com.atdit.booking.frontend.payment_process.abstract_controllers;

import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.contracts.Contract;
import com.atdit.booking.backend.financialdata.processing.PaymentMethodEvaluator;
import com.atdit.booking.frontend.super_controller.Cacheable;
import com.atdit.booking.frontend.super_controller.Controller;
import com.atdit.booking.frontend.super_controller.Navigatable;
import com.atdit.booking.frontend.payment_process.controllers.Page2aSelectPaymentController;
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

        selectedPayment = Page2aSelectPaymentController.selectedPayment;
        contract = Page2aSelectPaymentController.contract;
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
        loadScene(e, "payment_process/page_2a_payment_selection.fxml", "Zahlungsart auswählen");
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
            case "BuyNowPayLater" -> loadScene(e, "payment_process/page_4b_buy_now_pay_later_contract.fxml", "Vertragsdetails");
            case "Financing" -> loadScene(e, "payment_process/page_4c_financing_contract.fxml", "Vertragsdetails");
            case "One-Time" -> loadScene(e, "payment_process/page_4a_one_time_payment_contract.fxml", "Vertragsdetails");
        }
    }

}
