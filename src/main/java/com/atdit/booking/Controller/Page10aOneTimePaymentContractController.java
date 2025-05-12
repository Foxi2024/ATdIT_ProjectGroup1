package com.atdit.booking.Controller;

import com.atdit.booking.Navigatable;
import com.atdit.booking.OneTimePaymentContract;
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

public class Page10aOneTimePaymentContractController extends Controller implements Initializable, Navigatable {

    @FXML private Label customerNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label paymentMethodLabel;
    @FXML private TextArea contractTextArea;
    @FXML private RadioButton signatureCheckbox;
    @FXML private Button continueButton;

    private static final int TOTAL_AMOUNT = 5000;
    public static OneTimePaymentContract contract = (OneTimePaymentContract) Page8aSelectPaymentController.contract;
    public static Customer currentCustomer = Page7ControllerPageLogin.currentCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerNameLabel.setText(currentCustomer.getFirstName() + " " + currentCustomer.getName());
        emailLabel.setText(currentCustomer.getEmail());

        paymentMethodLabel.setText(contract.getPaymentMethod());
        totalAmountLabel.setText(String.format("€%.2f", (double) TOTAL_AMOUNT));

        contractTextArea.setText(contract.getContractText());
    }

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