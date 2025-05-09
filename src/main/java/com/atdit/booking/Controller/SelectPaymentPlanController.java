package com.atdit.booking.Controller;

import com.atdit.booking.FinancingContract;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class SelectPaymentPlanController extends Controller implements Initializable {

    @FXML private Label totalAmountLabel;
    @FXML private ComboBox<Integer> monthsCombo;
    @FXML private Label monthlyPaymentLabel;
    @FXML private Label interestRateLabel;
    @FXML private Label totalCostLabel;


    private static final double INTEREST_RATE = 5.0;
    public static int months;
    public static FinancingContract financingContract = (FinancingContract) SelectPaymentController.contract;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        totalAmountLabel.setText(String.valueOf(financingContract.getTotalAmount()));
        interestRateLabel.setText(String.format("%.2f", INTEREST_RATE) + "%");

        monthsCombo.getItems().addAll(12, 24, 36, 48, 60);
        monthsCombo.setValue(12);

        financingContract.setInterestRate(INTEREST_RATE);
        financingContract.setMonths(12);

        monthsCombo.setOnAction(e -> updateCalculations());

        updateCalculations();
    }

    private void updateCalculations() {


        months = monthsCombo.getValue();

        financingContract.setMonths(months);

        monthlyPaymentLabel.setText(String.format("€%.2f", financingContract.getMonthlyPayment()));
        totalCostLabel.setText(String.format("€%.2f", financingContract.getAmountWithInterest()));
    }

    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "payment_selection_page.fxml", "Payment Selection");
    }

    @FXML
    public void handleApply(MouseEvent e) {
        loadScene(e, "creditcard.fxml", "Contract Details");
    }
}