package com.atdit.booking.Controller;

import com.atdit.booking.financialdata.FinancialInformation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ControllerPage4 extends Controller implements Initializable {

    @FXML private TextField netIncomeField;
    @FXML private TextField fixedCostsField;
    @FXML private TextField minLivingCostField;
    @FXML private TextField netWorthField;
    @FXML private Button continueButton;
    @FXML private Button backButton;

    private static Map<String, Integer> savedData = new HashMap<>();
    private static boolean hasData = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (hasData) {
            restoreFormData();
        }
    }

    private void saveFormData() {
        try {
            savedData.put("netIncome", Integer.parseInt(netIncomeField.getText().trim()));
            savedData.put("fixedCosts", Integer.parseInt(fixedCostsField.getText().trim()));
            savedData.put("minLivingCost", Integer.parseInt(minLivingCostField.getText().trim()));
            savedData.put("netWorth", Integer.parseInt(netWorthField.getText().trim()));
            hasData = true;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Please enter valid numbers");
            alert.setContentText("All fields must contain valid integer values");
            alert.showAndWait();
        }
    }

    private void restoreFormData() {
        netIncomeField.setText(String.valueOf(savedData.get("netIncome")));
        fixedCostsField.setText(String.valueOf(savedData.get("fixedCosts")));
        minLivingCostField.setText(String.valueOf(savedData.get("minLivingCost")));
        netWorthField.setText(String.valueOf(savedData.get("netWorth")));
    }

    @FXML
    public void nextPage(MouseEvent e) {
        if (validateForm()) {
            saveFormData();
            // Create FinancialInformation object using setters
            FinancialInformation financialInfo = new FinancialInformation();

            try {
                financialInfo.setAvgNetIncome(savedData.get("netIncome"));
                financialInfo.setMonthlyFixCost(savedData.get("fixedCosts"));
                financialInfo.setMinCostOfLiving(savedData.get("minLivingCost"));
                // Set fixed and liquid assets from netWorth
                int netWorth = savedData.get("netWorth");
                // Assuming netWorth is split equally between fixed and liquid assets
                financialInfo.setFixedAssets(netWorth / 2);
                financialInfo.setLiquidAssets(netWorth / 2);
                // Default rent to 0 for now
                financialInfo.setRent(0);

                Stage stage = (Stage) continueButton.getScene().getWindow();
                Scene scene = getScene("page_5.fxml");
                stage.setTitle("Next Step");
                stage.setScene(scene);

            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Data");
                alert.setHeaderText("Could not create financial information");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void previousPage(MouseEvent e) {
        saveFormData();
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = getScene("page_3.fxml");
        stage.setTitle("Personal Information");
        stage.setScene(scene);
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
            int netWorth = Integer.parseInt(netWorthField.getText().trim());
            if (netWorth < 0) {
                errorMessage.append("- Net worth cannot be negative\n");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            errorMessage.append("- Invalid net worth value\n");
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