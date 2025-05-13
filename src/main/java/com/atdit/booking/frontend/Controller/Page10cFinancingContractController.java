package com.atdit.booking.frontend.Controller;

import com.atdit.booking.backend.financialdata.contracts.FinancingContract;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Page10cFinancingContractController extends AbstractContractController {


    @FXML private Label paymentPlanLabel;
    @FXML private Label monthlyPaymentLabel;
    @FXML private Label downPaymentLabel;

    private FinancingContract financingContract;

    @Override
    protected void initializeParameters() {

        financingContract = (FinancingContract) Page8aSelectPaymentController.contract;

        paymentPlanLabel.setText(String.format("%d Monate", financingContract.getMonths()));
        monthlyPaymentLabel.setText(String.format("%.2f€", financingContract.getMonthlyPayment()));
        downPaymentLabel.setText(String.format("%.2f€", financingContract.getDownPayment()));
    }
}
