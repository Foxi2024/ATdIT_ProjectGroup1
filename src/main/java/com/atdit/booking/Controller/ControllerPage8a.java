package com.atdit.booking.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPage8a extends Controller implements Initializable {
    @FXML private Label totalAmountLabel;
    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;

    private static final double TOTAL_AMOUNT = 5000.00;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        totalAmountLabel.setText(String.format("€%.2f", TOTAL_AMOUNT));

        paymentMethodCombo.getItems().addAll(
                "Credit Card",
                "Debit Card",
                "Bank Transfer"
        );
        paymentMethodCombo.setValue("Credit Card");
    }

    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "page_7.fxml", "Payment Selection");
    }

    @FXML
    public void handlePay(MouseEvent e) {
        // Validate payment information
        if (validatePaymentInfo()) {
            loadScene(e, "page_9.fxml", "Contract Details");
        }
    }

    private boolean validatePaymentInfo() {
        // Add payment validation logic here
        return true;
    }
}