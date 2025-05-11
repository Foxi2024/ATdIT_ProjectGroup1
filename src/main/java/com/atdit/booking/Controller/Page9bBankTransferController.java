package com.atdit.booking.Controller;

import com.atdit.booking.Cacheable;
import com.atdit.booking.Contract;
import com.atdit.booking.Navigatable;
import com.atdit.booking.financialdata.BankTransferDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.lang.annotation.Native;
import java.net.URL;
import java.util.ResourceBundle;

public class Page9bBankTransferController extends Controller implements Initializable, Navigatable, Cacheable {

    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private TextField ibanField;
    @FXML private TextField accountHolderField;
    @FXML private TextField bankNameField;
    @FXML private TextField bicField;
    @FXML private ProcessStepBarController processStepBarController;

    public static String selectedPayment = Page8aSelectPaymentController.selectedPayment;
    public static Contract contract = Page8aSelectPaymentController.contract;
    public static BankTransferDetails bankTransferDetails = new BankTransferDetails();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        processStepBarController.setCurrentStep("payment_method");

        paymentMethodCombo.getItems().addAll(
                "Credit Card",
                "Bank Transfer"
        );

        paymentMethodCombo.setValue("Bank Transfer");
        contract.setPaymentMethod("Bank Transfer");

        restoreData();
    }

    @FXML
    public void selectPaymentMethod(ActionEvent e) {

        String selected = paymentMethodCombo.getValue();

        if (selected.equals("Credit Card")) {

            loadScene(e, "creditcard.fxml", "Payment Method Selection");
        }
    }


    public void cacheData() {

        bankTransferDetails.setIban(ibanField.getText());
        bankTransferDetails.setAccountHolder(accountHolderField.getText());
        bankTransferDetails.setBankName(bankNameField.getText());
        bankTransferDetails.setBicSwift(bicField.getText());
    }

    public void restoreData() {

        ibanField.setText(bankTransferDetails.getIban());
        accountHolderField.setText(bankTransferDetails.getAccountHolder());
        bankNameField.setText(bankTransferDetails.getBankName());
        bicField.setText(bankTransferDetails.getBicSwift());

    }


    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "payment_selection_page.fxml", "Payment Selection");
    }

    @FXML
    public void nextPage(MouseEvent e) {

        cacheData();

        switch (selectedPayment) {
            case "One-Time" -> loadScene(e, "one_time_payment_contract_page.fxml", "Contract Details");
            case "Financing" -> loadScene(e, "financing_contract_page.fxml", "Contract Details");
        }
    }
}