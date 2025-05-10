package com.atdit.booking.Controller;

import com.atdit.booking.Contract;
import com.atdit.booking.FinancingContract;
import com.atdit.booking.Navigatable;
import com.atdit.booking.OneTimePaymentContract;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Page8aSelectPaymentController extends Controller implements Initializable, Navigatable {
    @FXML private RadioButton oneTimePaymentRadio;
    @FXML private RadioButton financingRadio;
    @FXML private ToggleGroup paymentMethodGroup;
    public static String selectedPayment;
    @FXML private ProcessStepBarController processStepBarController;

    public static Contract contract;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        processStepBarController.setCurrentStep("payment_selection");
    }

    @FXML
    public void nextPage(MouseEvent e) {

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
        loadScene(e, "customer_login.fxml", "Page 6");
    }

}