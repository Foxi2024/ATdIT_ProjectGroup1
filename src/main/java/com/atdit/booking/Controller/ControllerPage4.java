package com.atdit.booking.Controller;

import com.atdit.booking.Main;
import com.atdit.booking.customer.Customer;
import com.atdit.booking.customer.CustomerEvaluater;
import com.atdit.booking.financialdata.FinancialInformation;
import com.atdit.booking.financialdata.FinancialInformationEvaluator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;


public class ControllerPage4 extends Controller implements Initializable {

    @FXML private TextField netIncomeField;
    @FXML private TextField fixedCostsField;
    @FXML private TextField minLivingCostField;
    @FXML private TextField liquidAssetsField;
    @FXML private Button continueButton;
    @FXML private Button backButton;

    private static final Customer currentCustomer = Main.customer;
    private static final FinancialInformation financialInfo = currentCustomer.getFinancialInformation();
    private static final FinancialInformationEvaluator financialInformationEvaluator = new FinancialInformationEvaluator(financialInfo);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { restoreFormData(); }


    @FXML
    public void nextPage(MouseEvent e) {

        try {
            cacheData();
        } catch (IllegalArgumentException ex) {
            showError("Invalid Input", "Please correct the input", ex.getMessage());
            return;
        }

        try {
            financialInformationEvaluator.validateDeclaredFinancialInfo();
        }
        catch (IllegalArgumentException ex) {
            showError("Evaluation Failed", "Evaluation of financial information failed", ex.getMessage());
            return;
        }

        if(!financialInformationEvaluator.valDeclaredFinancialInfo(1000)){
            showError("Evaluation Failed", "Customer evaluation failed", "Please check your financial information.");
            return;
        }

        loadScene(e, "page_5.fxml", "Financial Proof");
    }


    @FXML
    public void previousPage(MouseEvent e) {

        cacheData();
        loadScene(e, "page_3.fxml", "Personal Information");
    }


    public void cacheData() throws IllegalArgumentException {

        financialInfo.setAvgNetIncome(Integer.parseInt(netIncomeField.getText().trim()));
        financialInfo.setMonthlyFixCost(Integer.parseInt(fixedCostsField.getText().trim()));
        financialInfo.setMinCostOfLiving(Integer.parseInt(minLivingCostField.getText().trim()));
        financialInfo.setLiquidAssets(Integer.parseInt(liquidAssetsField.getText().trim()));
    }


    private void restoreFormData() {

        netIncomeField.setText(String.valueOf(financialInfo.getAvgNetIncome()));
        fixedCostsField.setText(String.valueOf(financialInfo.getMonthlyFixCost()));
        minLivingCostField.setText(String.valueOf(financialInfo.getMinCostOfLiving()));
        liquidAssetsField.setText(String.valueOf(financialInfo.getLiquidAssets()));
    }

}