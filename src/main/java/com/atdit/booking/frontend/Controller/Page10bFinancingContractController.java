package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.financialdata.contracts.FinancingContract;
import com.atdit.booking.backend.customer.Customer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the financing contract view.
 * This class handles the display and interaction with the financing contract details.
 * It extends the base Controller class and implements Initializable and Navigatable interfaces.
 */
public class Page10bFinancingContractController extends Controller implements Initializable, Navigatable {
    /** Label displaying the customer's full name */
    @FXML private Label customerNameLabel;
    /** Label displaying the customer's email address */
    @FXML private Label emailLabel;
    /** Label showing the total amount including interest */
    @FXML private Label totalAmountLabel;
    /** Label displaying the payment plan duration */
    @FXML private Label paymentPlanLabel;
    /** Label showing the monthly payment amount */
    @FXML private Label monthlyPaymentLabel;
    /** Text area containing the full contract text */
    @FXML private TextArea contractTextArea;
    /** Label showing the selected payment method */
    @FXML private Label paymentMethodLabel;
    /** Checkbox for contract signature confirmation */
    @FXML private RadioButton signatureCheckbox;
    /** Button to proceed to the next page */
    @FXML private Button continueButton;
    /** Label showing the down payment amount */
    @FXML private Label downPaymentLabel;

    /** The current financing contract instance */
    public static FinancingContract financingContract = (FinancingContract) Page8aSelectPaymentController.contract;
    /** The current customer instance */
    public static Customer currentCustomer = Page7ControllerPageLogin.currentCustomer;

    /**
     * Initializes the controller and populates all fields with the current contract and customer data.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerNameLabel.setText(currentCustomer.getFirstName() + " " + currentCustomer.getName());
        emailLabel.setText(currentCustomer.getEmail());

        paymentMethodLabel.setText(financingContract.getPaymentMethod());
        downPaymentLabel.setText(String.format("%.2f€", financingContract.getDownPayment()));
        totalAmountLabel.setText(String.format("%.2f€", financingContract.getAmountWithInterest()));
        paymentPlanLabel.setText(String.format("%d Monate", financingContract.getMonths()));
        monthlyPaymentLabel.setText(String.format("%.2f€", financingContract.getMonthlyPayment()));

        contractTextArea.setText(financingContract.getContractText());
    }

    /**
     * Handles the contract signature action.
     * Enables or disables the continue button based on the signature checkbox state.
     *
     * @param e The mouse event that triggered this action
     */
    public void signContract(MouseEvent e) {

        if (signatureCheckbox.isSelected()) {
            continueButton.setDisable(false);
            return;
        }

        continueButton.setDisable(true);
    }

    /**
     * Navigates to the previous page based on the selected payment method.
     *
     * @param e The mouse event that triggered this action
     */
    @FXML
    public void previousPage(MouseEvent e) {

        if(financingContract.getPaymentMethod().equals("Kreditkarte")) {

            loadScene(e, "creditcard.fxml", "Zahlungsmethode auswählen");

        } else {

            loadScene(e, "banktransfer.fxml", "Zahlungsmethode auswählen");

        }
    }

    /**
     * Navigates to the confirmation page.
     *
     * @param e The mouse event that triggered this action
     */
    @FXML
    public void nextPage(MouseEvent e) {
        loadScene(e, "confirmation_page.fxml", "Bestätigung");
    }
}