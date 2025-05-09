package com.atdit.booking.Controller;

import com.atdit.booking.Contract;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class CreditCardController extends Controller implements Initializable {

    @FXML public ComboBox<String> paymentMethodCombo;
    @FXML public TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;
    @FXML private VBox expiryBox;
    @FXML private VBox cvvBox;
    @FXML private Label cardLabel;

    public static String selectedPayment = SelectPaymentController.selectedPayment;
    public static Contract contract = SelectPaymentController.contract;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        paymentMethodCombo.getItems().addAll(
                "Credit Card",
                "Bank Transfer"
        );

        paymentMethodCombo.setValue("Credit Card");
        contract.setPaymentMethod("Credit Card");

        setupPaymentMethodListener();
    }

    private void setupPaymentMethodListener() {
        paymentMethodCombo.setOnAction(e -> {

            String selected = paymentMethodCombo.getValue();

            if (selected.equals("Bank Transfer")) {

                loadScene(e, "banktransfer.fxml", "Payment Method Selection");
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