package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.financialdata.contracts.Contract;
import com.atdit.booking.backend.financialdata.financial_information.BankTransferDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Page9bBankTransferController extends Controller implements Initializable, Navigatable, Cacheable {

    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private TextField ibanField;
    @FXML private TextField accountHolderField;
    @FXML private TextField bankNameField;
    @FXML private TextField bicField;

    public static String selectedPayment = Page8aSelectPaymentController.selectedPayment;
    public static Contract contract = Page8aSelectPaymentController.contract;
    public static BankTransferDetails bankTransferDetails = new BankTransferDetails();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        paymentMethodCombo.getItems().addAll(
                "Kreditkarte",
                "Überweisung"
        );

        paymentMethodCombo.setValue("Überweisung");
        contract.setPaymentMethod("Überweisung");

        restoreData();
    }

    @FXML
    public void selectPaymentMethod(ActionEvent e) {

        String selected = paymentMethodCombo.getValue();

        if (selected.equals("Kreditkarte")) {

            loadScene(e, "creditcard.fxml", "Zahlungsmethode auswählen");
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
        loadScene(e, "payment_selection_page.fxml", "Zahlungsart auswählen");
    }

    @FXML
    public void nextPage(MouseEvent e) {

        if (!validateBankTransferInfo()) {
            return;
        }

        cacheData();

        switch (selectedPayment) {
            case "One-Time" -> loadScene(e, "one_time_payment_contract_page.fxml", "Vertragsdetails");
            case "Financing" -> loadScene(e, "financing_contract_page.fxml", "Vertragsdetails");
        }
    }




}