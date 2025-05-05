package com.atdit.booking.Controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerPage8a extends Controller {
    @FXML private Label totalAmountLabel;
    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;

    @FXML
    public void initialize() {
        paymentMethodCombo.getItems().addAll("Credit Card", "Debit Card");
        paymentMethodCombo.setValue("Credit Card");
        totalAmountLabel.setText("€5000.00"); // Example amount
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) totalAmountLabel.getScene().getWindow();
        //Scene scene = loadScene("page_7.fxml");
        stage.setTitle("Payment Selection");
        //stage.setScene(scene);
    }

    @FXML
    private void handlePay() {
        // Implement payment processing logic
    }
}