package com.atdit.booking.frontend.payment_process.controllers;

import com.atdit.booking.backend.financialdata.financial_information.CreditCardDetails;
import com.atdit.booking.frontend.payment_process.abstract_controllers.AbstractPaymentMethodController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


public class Page3aCreditCardController extends AbstractPaymentMethodController {

    @FXML public ComboBox<String> paymentMethodCombo;
    @FXML public TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;

    public static final CreditCardDetails creditCardDetails = new CreditCardDetails();

    @Override
    protected void initializeParameters() {

        evaluator.setCreditCardDetails(creditCardDetails);
        paymentMethod = "Kreditkarte";
    }

    @Override
    protected void validateInfo() {
        evaluator.validateCreditCardInfo();
    }

    @Override
    public void cacheData() {

        creditCardDetails.setCardNumber(cardNumberField.getText());
        creditCardDetails.setExpiryDate(expiryField.getText());
        creditCardDetails.setCvv(cvvField.getText());
    }

    public void restoreData() {

        cardNumberField.setText(creditCardDetails.getCardNumber());
        expiryField.setText(creditCardDetails.getExpiryDate());
        cvvField.setText(creditCardDetails.getCvv());

    }
}
