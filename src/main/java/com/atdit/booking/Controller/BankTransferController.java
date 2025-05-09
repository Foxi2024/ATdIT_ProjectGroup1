package com.atdit.booking.Controller;

import com.atdit.booking.Contract;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class BankTransferController extends Controller implements Initializable {

    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private TextField ibanField;
    @FXML private TextField accountHolderField;
    @FXML private TextField bankNameField;
    @FXML private TextField bicField;

    public static String selectedPayment = SelectPaymentController.selectedPayment;
    public static Contract contract = SelectPaymentController.contract;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        paymentMethodCombo.getItems().addAll(
                "Credit Card",
                "Bank Transfer"
        );

        paymentMethodCombo.setValue("Bank Transfer");
        contract.setPaymentMethod("Bank Transfer");

        setupPaymentMethodListener();
    }

    private void setupPaymentMethodListener() {
        paymentMethodCombo.setOnAction(e -> {

            String selected = paymentMethodCombo.getValue();

            if (!selected.equals("Bank Transfer")) {

                loadScene(e, "creditcard.fxml", "Payment Method Selection");
            }
        });
    }




    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "payment_selection_page.fxml", "Payment Selection");
    }

    @FXML
    public void handlePay(MouseEvent e) {

        switch (selectedPayment) {
            case "One-Time" -> loadScene(e, "one_time_payment_contract_page.fxml", "Contract Details");
            case "Financing" -> loadScene(e, "financing_contract_page.fxml", "Contract Details");
        }
    }

    private boolean validatePaymentInfo() {
        return !ibanField.getText().isEmpty()
                && ibanField.getText().length() >= 15
                && !accountHolderField.getText().isEmpty()
                && !bankNameField.getText().isEmpty()
                && !bicField.getText().isEmpty()
                && bicField.getText().length() >= 8;
    }
}