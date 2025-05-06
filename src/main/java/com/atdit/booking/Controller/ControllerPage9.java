package com.atdit.booking.Controller;

import com.atdit.booking.Main;
import com.atdit.booking.customer.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerPage9 extends Controller {

    @FXML private Label customerNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label paymentPlanLabel;
    @FXML private Label monthlyPaymentLabel;
    @FXML private TextArea contractTextArea;

    private static final Customer currentCustomer = Main.customer;
    private double totalAmount;
    private int months;
    private double monthlyPayment;

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        totalAmountLabel.setText(String.format("€%.2f", totalAmount));
    }

    public void setMonths(int months) {
        this.months = months;
        paymentPlanLabel.setText(months + " months");
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
        monthlyPaymentLabel.setText(String.format("€%.2f", monthlyPayment));
    }

    @FXML
    public void initialize() {
        if (currentCustomer != null) {
            customerNameLabel.setText(currentCustomer.getFirstName() + " " + currentCustomer.getName());
            emailLabel.setText(currentCustomer.getEmail());
        }

        // Generate contract text
        String contractText = generateContractText();
        contractTextArea.setText(contractText);
    }

    private String generateContractText() {
        StringBuilder sb = new StringBuilder();
        sb.append("CONTRACT\n\n");
        sb.append("Customer: ").append(customerNameLabel.getText()).append("\n");
        sb.append("Email: ").append(emailLabel.getText()).append("\n\n");
        sb.append("Total Amount: ").append(totalAmountLabel.getText()).append("\n");
        sb.append("Payment Plan: ").append(paymentPlanLabel.getText()).append("\n");
        sb.append("Monthly Payment: ").append(monthlyPaymentLabel.getText()).append("\n\n");
        sb.append("Terms and Conditions:\n");
        sb.append("... (Add detailed terms and conditions here) ...\n\n");
        sb.append("By signing below, you agree to the terms and conditions of this contract.\n");
        return sb.toString();
    }

    @FXML
    private void previousPage(MouseEvent e) {

        loadScene(e,"page_7.fxml", "Payment Selection");
        //loadScene(stage,"page_8b.fxml", "Payment Selection");
    }

    @FXML
    private void handleSign(MouseEvent e) {

        // Implement signature logic (e.g., save to database, show confirmation)
        loadScene(e,"page_10.fxml", "Payment Confirmation");
        System.out.println("Contract Signed!");
    }
}