package com.atdit.booking.Controller;

import com.atdit.booking.Contract;
import com.atdit.booking.FinancingContract;
import com.atdit.booking.OneTimePaymentContract;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

public class SelectPaymentController extends Controller {
    @FXML private RadioButton oneTimePaymentRadio;
    @FXML private RadioButton financingRadio;
    @FXML private ToggleGroup paymentMethodGroup;
    public static String selectedPayment;

    private static final int TOTAL_AMOUNT = 5000;
    public static Contract contract;

    @FXML
    private void handleContinue(MouseEvent e) {

        if (oneTimePaymentRadio.isSelected()) {
            selectedPayment = "One-Time";
            contract = new OneTimePaymentContract();
            loadScene(e, "creditcard.fxml", "One-Time Payment");

        } else {
            selectedPayment = "Financing";
            contract = new FinancingContract();
            loadScene(e, "financing_plan_selection_page.fxml", "Financing");
        }

    }

    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "page_6.fxml", "Page 6");
    }
}