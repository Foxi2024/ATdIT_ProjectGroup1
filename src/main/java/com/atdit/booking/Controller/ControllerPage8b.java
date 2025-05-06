package com.atdit.booking.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPage8b extends Controller implements Initializable {
    @FXML private Label totalAmountLabel;
    @FXML private ComboBox<Integer> monthsCombo;
    @FXML private Label monthlyPaymentLabel;
    @FXML private Label interestRateLabel;
    @FXML private Label totalCostLabel;

    private static final double TOTAL_AMOUNT = 5000.00;
    private static final double INTEREST_RATE = 5.9;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        totalAmountLabel.setText(String.format("€%.2f", TOTAL_AMOUNT));
        interestRateLabel.setText(String.format("%.1f%%", INTEREST_RATE));

        monthsCombo.getItems().addAll(12, 24, 36, 48, 60);
        monthsCombo.setValue(12);
        monthsCombo.setOnAction(e -> updateCalculations());

        updateCalculations();
    }

    private void updateCalculations() {
        int months = monthsCombo.getValue();
        double monthlyRate = INTEREST_RATE / 12 / 100;
        double monthlyPayment = (TOTAL_AMOUNT * monthlyRate * Math.pow(1 + monthlyRate, months))
                / (Math.pow(1 + monthlyRate, months) - 1);
        double totalCost = monthlyPayment * months;

        monthlyPaymentLabel.setText(String.format("€%.2f", monthlyPayment));
        totalCostLabel.setText(String.format("€%.2f", totalCost));
    }

    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "page_7.fxml", "Payment Selection");
    }

    @FXML
    public void handleApply(MouseEvent e) {
        loadScene(e, "page_9.fxml", "Contract Details");
    }
}