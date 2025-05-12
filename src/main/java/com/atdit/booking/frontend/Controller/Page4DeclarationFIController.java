package com.atdit.booking.frontend.Controller;

import com.atdit.booking.Main;
import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.exceptions.EvaluationException;
import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;
import com.atdit.booking.backend.financialdata.processing.FinancialInformationEvaluator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class Page4DeclarationFIController extends Controller implements Initializable, Navigatable, Cacheable {

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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        restoreData();
    }


    @FXML
    public void nextPage(MouseEvent e) {

        try {
            cacheData();
            financialInformationEvaluator.validateDeclaredFinancialInfo();
            financialInformationEvaluator.evaluateDeclaredFinancialInfo();
        }
        catch (NumberFormatException ex) {
            showError("Fehlerhafte Eingaben", "Sie haben fehlerhafte Angaben gemacht.", ex.getMessage());
            return;
        }
        catch (ValidationException ex) {
            showError("Validierung fehlgeschlagen", "Die Validierung Ihrer finanziellen Daten ist fehlgeschlagen.", ex.getMessage());
            return;
        }
        catch (EvaluationException ex) {
            showError("Evaluierung fehlgeschlagen", "Die Evaluierung Ihrer finanziellen Daten ist fehlgeschlagen.", ex.getMessage());
            return;
        }

        loadScene(e, "page_5.fxml", "Nachweis finanzieller Angaben");
    }


    @FXML
    public void previousPage(MouseEvent e) {

        cacheData();
        loadScene(e, "page_3.fxml", "Finanzielle Angaben");
    }


    public void cacheData() throws NumberFormatException {

        financialInfo.setAvgNetIncome(Integer.parseInt(netIncomeField.getText().trim()));
        financialInfo.setMonthlyFixCost(Integer.parseInt(fixedCostsField.getText().trim()));
        financialInfo.setMinCostOfLiving(Integer.parseInt(minLivingCostField.getText().trim()));
        financialInfo.setLiquidAssets(Integer.parseInt(liquidAssetsField.getText().trim()));
    }


    public void restoreData() {

        netIncomeField.setText(String.valueOf(financialInfo.getAvgNetIncome()));
        fixedCostsField.setText(String.valueOf(financialInfo.getMonthlyFixCost()));
        minLivingCostField.setText(String.valueOf(financialInfo.getMinCostOfLiving()));
        liquidAssetsField.setText(String.valueOf(financialInfo.getLiquidAssets()));
    }

}