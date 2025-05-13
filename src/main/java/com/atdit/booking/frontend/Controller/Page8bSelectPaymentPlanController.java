package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.financialdata.contracts.FinancingContract;
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

/**
 * Controller class for managing the financing payment plan selection page.
 * This class handles the selection of payment duration and displays financial calculations.
 * It extends the base Controller class and implements Initializable and Navigatable interfaces.
 */
public class Page8bSelectPaymentPlanController extends Controller implements Initializable, Navigatable {

    /** Label displaying the total amount to be financed */
    @FXML private Label totalAmountLabel;

    /** ComboBox for selecting the financing duration in months */
    @FXML private ComboBox<Integer> monthsCombo;

    /** Label displaying the monthly payment amount */
    @FXML private Label monthlyPaymentLabel;

    /** Label displaying the interest rate */
    @FXML private Label interestRateLabel;

    /** Label displaying the total cost including interest */
    @FXML private Label totalCostLabel;

    /** Label displaying the down payment amount */
    @FXML private Label downPaymentLabel;

    /** Static reference to the financing contract from the previous page */
    public static FinancingContract financingContract;

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     * Sets up the months selection ComboBox with predefined values and performs initial calculations.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        financingContract = (FinancingContract) Page8aSelectPaymentController.contract;

        monthsCombo.getItems().addAll(12, 24, 36, 48, 60);
        monthsCombo.setValue(12);

        updateCalculations();
    }

    /**
     * Event handler for when a new month duration is selected.
     * Triggers recalculation of financial values.
     *
     * @param e The action event triggered by selecting a new value in the months ComboBox
     */
    @FXML
    public void chooseMonths(ActionEvent e) {

        updateCalculations();
    }

    /**
     * Updates all financial calculations based on the selected financing duration.
     * Formats and displays the monetary values using German locale.
     */
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

    /**
     * Navigates to the previous page (payment selection).
     *
     * @param e The mouse event that triggered the navigation
     */
    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "payment_selection_page.fxml", "Zahlungsart auswählen");
    }

    /**
     * Navigates to the next page (credit card details).
     *
     * @param e The mouse event that triggered the navigation
     */
    @FXML
    public void nextPage(MouseEvent e) {
        loadScene(e, "creditcard.fxml", "Zahlungsmethode auswählen");
    }
}