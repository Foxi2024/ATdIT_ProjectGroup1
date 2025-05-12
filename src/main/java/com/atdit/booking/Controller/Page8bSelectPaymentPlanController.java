package com.atdit.booking.Controller;

import com.atdit.booking.FinancingContract;
import com.atdit.booking.Navigatable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import java.net.URL;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Page8bSelectPaymentPlanController extends Controller implements Initializable, Navigatable {

    @FXML private Label totalAmountLabel;
    @FXML private ComboBox<Integer> monthsCombo;
    @FXML private Label monthlyPaymentLabel;
    @FXML private Label interestRateLabel;
    @FXML private Label totalCostLabel;
    @FXML private Label downPaymentLabel;

    public static FinancingContract financingContract = (FinancingContract) Page8aSelectPaymentController.contract;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        monthsCombo.getItems().addAll(12, 24, 36, 48, 60);
        monthsCombo.setValue(12);

        updateCalculations();
    }

    @FXML
    public void chooseMonths(ActionEvent e) {

        updateCalculations();
    }

    private void updateCalculations() {

        financingContract.setMonths(monthsCombo.getValue());

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);



        downPaymentLabel.setText(nf.format(financingContract.getDownPayment()));
        totalAmountLabel.setText(nf.format(financingContract.getFinancedAmount()));
        interestRateLabel.setText(String.format("%.2f", financingContract.getInterestRate()) + "%");
        monthlyPaymentLabel.setText(nf.format(financingContract.getMonthlyPayment()));
        totalCostLabel.setText(nf.format(financingContract.getAmountWithInterest()));
    }

    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "payment_selection_page.fxml", "Zahlungsart auswählen");
    }

    @FXML
    public void nextPage(MouseEvent e) {
        loadScene(e, "creditcard.fxml", "Zahlungsmethode auswählen");
    }
}