package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.financialdata.contracts.BuyNowPayLaterContract;
import com.atdit.booking.backend.financialdata.contracts.Contract;
import com.atdit.booking.backend.financialdata.contracts.FinancingContract;
import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.financialdata.contracts.OneTimePaymentContract;
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



    @FXML private Label paymentPlanLabel;
    @FXML private Label monthlyPaymentLabel;
    @FXML private Label downPaymentLabel;



    public static Contract contract;
    public static Customer currentCustomer;

    /**
     * Initializes the controller after FXML loading.
     * Populates the UI elements with customer and contract information.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        currentCustomer = Page7ControllerPageLogin.currentCustomer;
        contract = Page8aSelectPaymentController.contract;

        initializeParameters();

        customerNameLabel.setText(currentCustomer.getFirstName() + " " + currentCustomer.getName());
        emailLabel.setText(currentCustomer.getEmail());
        paymentMethodLabel.setText(contract.getPaymentMethod());
        totalAmountLabel.setText(String.format("€%.2f", contract.TOTAL_AMOUNT));
        contractTextArea.setText(contract.getContractText());
    }

    protected abstract void initializeParameters();


    @FXML
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
            loadScene(e, "creditcard.fxml", "Zahlungsmethode auswählen");
        } else {
            loadScene(e, "banktransfer.fxml", "Zahlungsmethode auswählen");
        }
    }


    @FXML
    public void nextPage(MouseEvent e) {
        loadScene(e, "confirmation_page.fxml", "Confirmation");
    }
}

