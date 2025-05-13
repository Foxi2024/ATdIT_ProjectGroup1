package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.contracts.Contract;
import com.atdit.booking.backend.financialdata.financial_information.CreditCardDetails;
import com.atdit.booking.backend.financialdata.financial_information.PaymentMethodEvaluator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Page9aCreditCardController extends Controller implements Initializable, Navigatable, Cacheable {

    @FXML public ComboBox<String> paymentMethodCombo;
    @FXML public TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;
    @FXML private VBox expiryBox;
    @FXML private VBox cvvBox;
    @FXML private Label cardLabel;
    @FXML private Button backButton;
   

    public static String selectedPayment = Page8aSelectPaymentController.selectedPayment;
    public static Contract contract = Page8aSelectPaymentController.contract;
    public static CreditCardDetails creditCardDetails = new CreditCardDetails();
    public static PaymentMethodEvaluator evaluator;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println(contract);
        System.out.println(selectedPayment);

        if(selectedPayment.equals("One-Time")){
            backButton.setDisable(true);

        }

        evaluator = new PaymentMethodEvaluator();
        evaluator.setCreditCardDetails(creditCardDetails);

        paymentMethodCombo.getItems().addAll(
                "Kreditkarte",
                "Überweisung"
        );

        paymentMethodCombo.setValue("Kreditkarte");
        contract.setPaymentMethod("Kreditkarte");

        restoreData();
    }

    @FXML
    public void selectPaymentMethod(ActionEvent e) {

        String selected = paymentMethodCombo.getValue();

        if (selected.equals("Überweisung")) {

            loadScene(e, "banktransfer.fxml", "Zahlungsmethode auswählen");
        }
    }

    public void cacheData() {

        creditCardDetails.setCardNumber(cardNumberField.getText());
        creditCardDetails.setExpiryDate(expiryField.getText());
        creditCardDetails.setCvv(cvvField.getText());

    }


    public void restoreData() {

        cardNumberField.setText(creditCardDetails.getCardNumber());
        expiryField.setText(creditCardDetails.getExpiryDate());
        cvvField.setText(creditCardDetails.getCvv());

    }

    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "payment_selection_page.fxml", "Zahlungsart auswählen");
    }

    @FXML
    public void nextPage(MouseEvent e) {

        cacheData();

        try {
            evaluator.validateCreditCardInfo();
        }
        catch (ValidationException ex) {
            showError("Validierungsfehler", "Fehler beim validieren Ihrer angegebenen Daten", ex.getMessage());
            return;
        }

        switch (selectedPayment) {
            case "BuyNowPayLater" -> loadScene(e, "Buy_now_pay_later_contract_page.fxml", "Vertragsdetails");
            case "Financing" -> loadScene(e, "financing_contract_page.fxml", "Vertragsdetails");
            case "One-Time" -> loadScene(e, "one_time_payment_contract_page.fxml", "Vertragsdetails");
        }
    }

}