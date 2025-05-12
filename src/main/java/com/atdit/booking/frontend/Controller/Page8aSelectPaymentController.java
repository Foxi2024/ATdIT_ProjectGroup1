package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.financialdata.contracts.Contract;
import com.atdit.booking.backend.financialdata.contracts.FinancingContract;
import com.atdit.booking.backend.financialdata.contracts.OneTimePaymentContract;
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

    public static Contract contract;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void nextPage(MouseEvent e) {

        if (oneTimePaymentRadio.isSelected()) {
            selectedPayment = "One-Time";
            contract = new OneTimePaymentContract();
            loadScene(e, "creditcard.fxml", "Zahlungsmethode auswählen");

        } else {
            selectedPayment = "Financing";
            contract = new FinancingContract();
            loadScene(e, "financing_plan_selection_page.fxml", "Finanzierung auswählen");
        }

    }

    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "customer_login.fxml", "Page 6");
    }

}