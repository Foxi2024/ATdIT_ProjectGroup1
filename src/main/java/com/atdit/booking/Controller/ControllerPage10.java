package com.atdit.booking.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Random;

public class ControllerPage10 extends Controller {

    @FXML private Label totalAmountLabel;
    @FXML private Label paymentIdLabel;
    @FXML private Label paymentStatusLabel;

    private double totalAmount;
    private int paymentId;
    private boolean paymentCorrected = false;

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        totalAmountLabel.setText(String.format("€%.2f", totalAmount));
    }

    @FXML
    public void initialize() {
        Random random = new Random();
        paymentId = random.nextInt(900000) + 100000;
        paymentIdLabel.setText(String.valueOf(paymentId));
    }

    @FXML
    private void handleConfirmPayment() {
        // Implement payment validation logic
        if (validatePayment()) {
            paymentStatusLabel.setText("Payment Successful");
            showAlert("Payment Confirmation", "Payment was successful. Thank you!");
            // Load confirmation page or final process
        } else {
            paymentStatusLabel.setText("Payment Failed");
            showAlert("Payment Error", "Payment validation failed. Please contact support.");
        }
    }

    @FXML
    private void handleCorrectPayment() {
        if (!paymentCorrected) {
            // Allow the user to correct the payment (e.g., show a dialog)
            showAlert("Correct Payment", "Please correct your payment details and try again.");
            paymentCorrected = true; // Mark that correction has been attempted
        } else {
            showAlert("Payment Error", "You have already attempted to correct the payment once. Please contact support.");
        }
    }

    private boolean validatePayment() {
        // Implement actual payment validation logic here (e.g., check with payment gateway)
        // For now, just return true for testing purposes
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void previousPage(MouseEvent e) {

        loadScene(e, "financing_contract_page.fxml", "Contract and Signature");
    }
}