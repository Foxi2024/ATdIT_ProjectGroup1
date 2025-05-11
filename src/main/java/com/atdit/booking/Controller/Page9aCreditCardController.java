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
    @FXML private ProcessStepBarController processStepBarController;

    public static String selectedPayment = Page8aSelectPaymentController.selectedPayment;
    public static Contract contract = Page8aSelectPaymentController.contract;
    public static CreditCardDetails creditCardDetails = new CreditCardDetails();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        processStepBarController.setCurrentStep("payment_method");

        paymentMethodCombo.getItems().addAll(
                "Credit Card",
                "Bank Transfer"
        );

        paymentMethodCombo.setValue("Credit Card");
        contract.setPaymentMethod("Credit Card");

        restoreData();
    }

    @FXML
    public void selectPaymentMethod(ActionEvent e) {

        String selected = paymentMethodCombo.getValue();

        if (selected.equals("Bank Transfer")) {

            loadScene(e, "banktransfer.fxml", "Payment Method Selection");
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
        loadScene(e, "payment_selection_page.fxml", "Payment Selection");
    }

    @FXML
    public void nextPage(MouseEvent e) {

        cacheData();

        switch (selectedPayment) {
            case "One-Time" -> loadScene(e, "one_time_payment_contract_page.fxml", "Contract Details");
            case "Financing" -> loadScene(e, "financing_contract_page.fxml", "Contract Details");
        }
    }

    private boolean validatePaymentInfo() {
        String selected = paymentMethodCombo.getValue();
        if (selected.equals("Bank Transfer")) {
            return cardNumberField.getText().length() >= 15;
        } else {
            return cardNumberField.getText().replace(" ", "").length() == 16
                    && expiryField.getText().length() == 5
                    && cvvField.getText().length() == 3;
        }
    }
}