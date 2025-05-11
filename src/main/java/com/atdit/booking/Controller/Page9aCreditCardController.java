package com.atdit.booking.Controller;

import com.atdit.booking.Cacheable;
import com.atdit.booking.Contract;
import com.atdit.booking.Navigatable;
import com.atdit.booking.financialdata.CreditCardDetails;
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

public class Page9aCreditCardController extends Controller implements Initializable, Navigatable, Cacheable {

    @FXML public ComboBox<String> paymentMethodCombo;
    @FXML public TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;
    @FXML private VBox expiryBox;
    @FXML private VBox cvvBox;
    @FXML private Label cardLabel;
   

    public static String selectedPayment = Page8aSelectPaymentController.selectedPayment;
    public static Contract contract = Page8aSelectPaymentController.contract;
    public static CreditCardDetails creditCardDetails = new CreditCardDetails();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        paymentMethodCombo.getItems().addAll(
                "Kreditkarte",
                "Überweisung"
        );

        paymentMethodCombo.setValue("Kreditkarte");
        contract.setPaymentMethod("Kreditkarte");

        restoreData();
    }

    @FXML
    public void selectPaymentMethod(ActionEvent e) {

        String selected = paymentMethodCombo.getValue();

        if (selected.equals("Überweisung")) {

            loadScene(e, "banktransfer.fxml", "Zahlungsmethode auswählen");
        }
    }

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

    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "payment_selection_page.fxml", "Zahlungsart auswählen");
    }

    @FXML
    public void nextPage(MouseEvent e) {

        if (!validatePaymentInfo()) {
            return;
        }

        cacheData();

        switch (selectedPayment) {
            case "One-Time" -> loadScene(e, "one_time_payment_contract_page.fxml", "Vertragsdetails");
            case "Financing" -> loadScene(e, "financing_contract_page.fxml", "Vertragsdetails");
        }
    }

    private boolean validatePaymentInfo() {
        StringBuilder errorMessage = new StringBuilder("Bitte korrigieren Sie folgende Fehler:\n");
        boolean hasError = false;

        String cardNumber = cardNumberField.getText();
        if (cardNumber == null || !cardNumber.matches("^\\d{16}$")) {
            errorMessage.append("- Ungültige Kartennummer (16 Ziffern erforderlich)\n");
            hasError = true;
        }

        String expiry = expiryField.getText();
        if (expiry == null || !expiry.matches("^(0[1-9]|1[0-2])/([0-9]{2})$")) {
            errorMessage.append("- Ungültiges Ablaufdatum (Format: MM/YY)\n");
            hasError = true;
        } else {
            // Check if card is not expired
            try {
                String[] parts = expiry.split("/");
                int month = Integer.parseInt(parts[0]);
                int year = Integer.parseInt(parts[1]) + 2000;

                java.time.YearMonth cardDate = java.time.YearMonth.of(year, month);
                java.time.YearMonth now = java.time.YearMonth.now();

                if (cardDate.isBefore(now)) {
                    errorMessage.append("- Kreditkarte ist abgelaufen\n");
                    hasError = true;
                }
            } catch (Exception e) {
                errorMessage.append("- Ungültiges Ablaufdatum\n");
                hasError = true;
            }
        }

        String cvv = cvvField.getText();
        if (cvv == null || !cvv.matches("^\\d{3,4}$")) {
            errorMessage.append("- Ungültiger CVV-Code (3-4 Ziffern erforderlich)\n");
            hasError = true;
        }

        if (hasError) {
            showError("Validierung fehlgeschlagen",
                    "Validierung der Kreditkartendaten ist fehlgeschlagen.",
                    errorMessage.toString());
            return false;
        }

        return true;
    }
}