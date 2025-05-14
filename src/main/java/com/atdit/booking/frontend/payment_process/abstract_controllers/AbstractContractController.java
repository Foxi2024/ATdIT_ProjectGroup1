package com.atdit.booking.frontend.payment_process.abstract_controllers;

import com.atdit.booking.backend.financialdata.contracts.Contract;
import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.frontend.super_controller.Controller;
import com.atdit.booking.frontend.payment_process.Page1ControllerPageLogin;
import com.atdit.booking.frontend.payment_process.Page2aSelectPaymentController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractContractController extends Controller implements Initializable {

    @FXML protected Label customerNameLabel;
    @FXML protected Label emailLabel;
    @FXML protected Label totalAmountLabel;
    @FXML protected Label paymentMethodLabel;
    @FXML protected TextArea contractTextArea;
    @FXML protected RadioButton signatureCheckbox;
    @FXML protected Button continueButton;

    public static Contract contract;
    public static Customer currentCustomer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        currentCustomer = Page1ControllerPageLogin.currentCustomer;
        contract = Page2aSelectPaymentController.contract;

        initializeParameters();

        // for better readability

        String customerText = currentCustomer.getTitle() + " " + currentCustomer.getFirstName() + " " + currentCustomer.getName();

        customerNameLabel.setText(customerText);
        emailLabel.setText(currentCustomer.getEmail());
        paymentMethodLabel.setText(contract.getPaymentMethod());
        totalAmountLabel.setText(String.format("€%.2f", contract.TOTAL_AMOUNT));
        contractTextArea.setText(contract.getContractText());
    }

    protected abstract void initializeParameters();


    @FXML
    @SuppressWarnings("unused")
    public void signContract(MouseEvent e) {

        if (signatureCheckbox.isSelected()) {
            continueButton.setDisable(false);
            return;
        }

        continueButton.setDisable(true);
    }


    @FXML
    public void previousPage(MouseEvent e) {

        if(contract.getPaymentMethod().equals("Kreditkarte")) {
            loadScene(e, "payment_process/page_3a_creditcard.fxml", "Zahlungsmethode auswählen");
        } else {
            loadScene(e, "payment_process/page_3b_banktransfer.fxml", "Zahlungsmethode auswählen");
        }
    }


    @FXML
    public void nextPage(MouseEvent e) {
        loadScene(e, "payment_process/page_5_confirmation.fxml", "Confirmation");
    }
}

