package com.atdit.booking.Controller;

import com.atdit.booking.Main;
import com.atdit.booking.financialdata.FinancialInformation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPage4 extends Controller implements Initializable {

    @FXML private TextField netIncomeField;
    @FXML private TextField fixedCostsField;
    @FXML private TextField minLivingCostField;
    @FXML private TextField liquidAssetsField;
    @FXML private Button continueButton;
    @FXML private Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { restoreFormData(); }


    @FXML
    public void nextPage(MouseEvent e) {

        if (validateForm()) {
            try {
                cacheData();

                Stage stage = (Stage) continueButton.getScene().getWindow();
                Scene scene = getScene("page_5.fxml");
                stage.setTitle("Next Step");
                stage.setScene(scene);

            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Data");
                alert.setHeaderText("Could not save financial information");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void previousPage(MouseEvent e) {
        cacheData();

        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = getScene("page_3.fxml");
        stage.setTitle("Personal Information");
        stage.setScene(scene);
    }

    public void cacheData() throws IllegalArgumentException {
        FinancialInformation financialInfo = Main.customer.getFinancialInformation();

        financialInfo.setAvgNetIncome(Integer.parseInt(netIncomeField.getText().trim()));
        financialInfo.setMonthlyFixCost(Integer.parseInt(fixedCostsField.getText().trim()));
        financialInfo.setMinCostOfLiving(Integer.parseInt(minLivingCostField.getText().trim()));
        financialInfo.setLiquidAssets(Integer.parseInt(liquidAssetsField.getText().trim()));
    }

    private void restoreFormData() {
        FinancialInformation financialInfo = Main.customer.getFinancialInformation();

        netIncomeField.setText(String.valueOf(financialInfo.getAvgNetIncome()));
        fixedCostsField.setText(String.valueOf(financialInfo.getMonthlyFixCost()));
        minLivingCostField.setText(String.valueOf(financialInfo.getMinCostOfLiving()));
        liquidAssetsField.setText(String.valueOf(financialInfo.getLiquidAssets()));
    }


    private boolean validateForm() {
        StringBuilder errorMessage = new StringBuilder("Please fix the following issues:\n");
        boolean isValid = true;

        try {
            int netIncome = Integer.parseInt(netIncomeField.getText().trim());
            if (netIncome < 0) {
                errorMessage.append("- Net income cannot be negative\n");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            errorMessage.append("- Invalid net income value\n");
            isValid = false;
        }

        try {
            int fixedCosts = Integer.parseInt(fixedCostsField.getText().trim());
            if (fixedCosts < 0) {
                errorMessage.append("- Fixed costs cannot be negative\n");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            errorMessage.append("- Invalid fixed costs value\n");
            isValid = false;
        }

        try {
            int minLiving = Integer.parseInt(minLivingCostField.getText().trim());
            if (minLiving < 0) {
                errorMessage.append("- Minimum living cost cannot be negative\n");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            errorMessage.append("- Invalid minimum living cost value\n");
            isValid = false;
        }

        try {
            int liquidAssets = Integer.parseInt(liquidAssetsField.getText().trim());
            if (liquidAssets < 0) {
                errorMessage.append("- Liquid assets cannot be negative\n");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            errorMessage.append("- Invalid liquid assets value\n");
            isValid = false;
        }

        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Information");
            alert.setHeaderText("Please correct the input");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }

        return isValid;
    }
}