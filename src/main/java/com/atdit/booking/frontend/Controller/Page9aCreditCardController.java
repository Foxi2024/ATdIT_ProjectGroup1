package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.financialdata.financial_information.CreditCardDetails;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Page9aCreditCardController extends AbstractPaymentMethodController {

    @FXML public ComboBox<String> paymentMethodCombo;
    @FXML public TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;
    @FXML private VBox expiryBox;
    @FXML private VBox cvvBox;
    @FXML private Label cardLabel;
    @FXML private Button backButton;

    public static CreditCardDetails creditCardDetails = new CreditCardDetails();

    @Override
    protected void initializeParameters() {

        evaluator.setCreditCardDetails(creditCardDetails);
        paymentMethod = "Kreditkarte";
        otherPage = "banktransfer.fxml";
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
