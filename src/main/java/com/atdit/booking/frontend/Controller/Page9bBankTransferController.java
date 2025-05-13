package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.financialdata.financial_information.BankTransferDetails;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class Page9bBankTransferController extends AbstractPaymentMethodController {

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

    public static BankTransferDetails bankTransferDetails = new BankTransferDetails();

    @Override
    protected void initializeParameters() {

        evaluator.setBankTransferDetails(bankTransferDetails);
        paymentMethod = "Überweisung";
        otherPage = "creditcard.fxml";
    }

    @Override
    protected void validateInfo() {
        evaluator.validateBankTransferInfo();
    }

    @Override
    public void cacheData() {
        bankTransferDetails.setIban(ibanField.getText());
        bankTransferDetails.setAccountHolder(accountHolderField.getText());
        bankTransferDetails.setBankName(bankNameField.getText());
        bankTransferDetails.setBicSwift(bicField.getText());
    }

    @Override
    public void restoreData() {
        ibanField.setText(bankTransferDetails.getIban());
        accountHolderField.setText(bankTransferDetails.getAccountHolder());
        bankNameField.setText(bankTransferDetails.getBankName());
        bicField.setText(bankTransferDetails.getBicSwift());
    }
}
