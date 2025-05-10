package com.atdit.booking.Controller;

import com.atdit.booking.FinancingContract;
import com.atdit.booking.customer.Customer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Page10bFinancingContractController extends Controller implements Initializable {
    @FXML private Label customerNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label paymentPlanLabel;
    @FXML private Label monthlyPaymentLabel;
    @FXML private TextArea contractTextArea;
    @FXML private Label paymentMethodLabel;
    @FXML private RadioButton signatureCheckbox;
    @FXML private Button continueButton;
    @FXML private ProcessStepBarController processStepBarController;

    private static final int TOTAL_AMOUNT = 5000;
    public static FinancingContract financingContract = (FinancingContract) Page8SelectPaymentController.contract;
    public static Customer currentCustomer = Page8bControllerPageLogin.currentCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        processStepBarController.setCurrentStep("contract");

        customerNameLabel.setText(currentCustomer.getFirstName() + " " + currentCustomer.getName());
        emailLabel.setText(currentCustomer.getEmail());

        paymentMethodLabel.setText(financingContract.getPaymentMethod());
        totalAmountLabel.setText(String.format("€%.2f", financingContract.getAmountWithInterest()));
        paymentPlanLabel.setText(String.format("%d months", financingContract.getMonths()));
        monthlyPaymentLabel.setText(String.format("€%.2f", financingContract.getMonthlyPayment()));

        contractTextArea.setText(financingContract.getContractText());
    }


    public void signContract(MouseEvent e) {

        if (signatureCheckbox.isSelected()) {
            continueButton.setDisable(false);
            return;
        }

        continueButton.setDisable(true);
    }

    @FXML
    public void previousPage(MouseEvent e) {

        if(financingContract.getPaymentMethod().equals("Credit Card")) {

            loadScene(e, "creditcard.fxml", "Payment Method Selection");

        } else {

            loadScene(e, "banktransfer.fxml", "Payment Method Selection");

        }
    }

    @FXML
    public void handleSign(MouseEvent e) {
        loadScene(e, "confirmation_page.fxml", "Confirmation");
    }
}