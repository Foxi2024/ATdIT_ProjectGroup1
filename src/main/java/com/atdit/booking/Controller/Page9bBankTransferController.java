package com.atdit.booking.Controller;

import com.atdit.booking.Cacheable;
import com.atdit.booking.Contract;
import com.atdit.booking.Navigatable;
import com.atdit.booking.financialdata.BankTransferDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.lang.annotation.Native;
import java.net.URL;
import java.util.ResourceBundle;

public class Page9bBankTransferController extends Controller implements Initializable, Navigatable, Cacheable {

    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private TextField ibanField;
    @FXML private TextField accountHolderField;
    @FXML private TextField bankNameField;
    @FXML private TextField bicField;
    @FXML private ProcessStepBarController processStepBarController;

    public static String selectedPayment = Page8aSelectPaymentController.selectedPayment;
    public static Contract contract = Page8aSelectPaymentController.contract;
    public static BankTransferDetails bankTransferDetails = new BankTransferDetails();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        paymentMethodCombo.getItems().addAll(
                "Kreditkarte",
                "Überweisung"
        );

        paymentMethodCombo.setValue("Überweisung");
        contract.setPaymentMethod("Überweisung");

        restoreData();
    }

    @FXML
    public void selectPaymentMethod(ActionEvent e) {

        String selected = paymentMethodCombo.getValue();

        if (selected.equals("Kreditkarte")) {

            loadScene(e, "creditcard.fxml", "Zahlungsmethode auswählen");
        }
    }


    public void cacheData() {

        bankTransferDetails.setIban(ibanField.getText());
        bankTransferDetails.setAccountHolder(accountHolderField.getText());
        bankTransferDetails.setBankName(bankNameField.getText());
        bankTransferDetails.setBicSwift(bicField.getText());
    }

    public void restoreData() {

        ibanField.setText(bankTransferDetails.getIban());
        accountHolderField.setText(bankTransferDetails.getAccountHolder());
        bankNameField.setText(bankTransferDetails.getBankName());
        bicField.setText(bankTransferDetails.getBicSwift());
    }


    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "payment_selection_page.fxml", "Zahlungsart auswählen");
    }

    @FXML
    public void nextPage(MouseEvent e) {

        if (!validateBankTransferInfo()) {
            return;
        }

        cacheData();

        switch (selectedPayment) {
            case "One-Time" -> loadScene(e, "one_time_payment_contract_page.fxml", "Vertragsdetails");
            case "Financing" -> loadScene(e, "financing_contract_page.fxml", "Vertragsdetails");
        }
    }

    private boolean validateBankTransferInfo() {
        StringBuilder errorMessage = new StringBuilder("Bitte korrigieren Sie folgende Fehler:\n");
        boolean hasError = false;

        // Validate IBAN (simplified for German IBAN)
        String iban = ibanField.getText().replaceAll("\\s", "").toUpperCase();
        if (!iban.matches("^DE\\d{20}$")) {
            errorMessage.append("- Ungültige IBAN (Format: DE + 20 Ziffern)\n");
            hasError = true;
        }

        // Validate BIC/SWIFT (8 or 11 alphanumeric characters)
        String bic = bicField.getText().replaceAll("\\s", "").toUpperCase();
        if (!bic.matches("^[A-Z0-9]{8}(?:[A-Z0-9]{3})?$")) {
            errorMessage.append("- Ungültiger BIC/SWIFT Code (8 oder 11 Zeichen)\n");
            hasError = true;
        }

        // Validate account holder (not empty, only letters and spaces)
        String accountHolder = accountHolderField.getText().trim();
        if (accountHolder.isEmpty() || !accountHolder.matches("^[A-Za-zÄäÖöÜüß\\s-]{2,50}$")) {
            errorMessage.append("- Ungültiger Kontoinhaber (2-50 Zeichen, nur Buchstaben erlaubt)\n");
            hasError = true;
        }

        // Validate bank name (not empty)
        String bankName = bankNameField.getText().trim();
        if (bankName.isEmpty() || bankName.length() < 2) {
            errorMessage.append("- Ungültiger Bankname (mindestens 2 Zeichen)\n");
            hasError = true;
        }

        if (hasError) {
            showError("Validierung fehlgeschlagen",
                    "Validierung der Überweisungsdaten ist fehlgeschlagen.",
                    errorMessage.toString());
            return false;
        }

        return true;
    }


}