package com.atdit.booking.frontend.customer_registration;

import com.atdit.booking.Main;
import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.exceptions.EvaluationException;
import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;
import com.atdit.booking.backend.financialdata.processing.FinancialInformationEvaluator;
import com.atdit.booking.frontend.super_controller.Cacheable;
import com.atdit.booking.frontend.super_controller.Controller;
import com.atdit.booking.frontend.super_controller.Navigatable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for Page 4 of the application where users declare their financial information.
 * This class handles the UI interactions and validation of financial data input.
 * Extends the base Controller class and implements Initializable, Navigatable and Cacheable interfaces.
 */
public class Page4DeclarationFIController extends Controller implements Initializable, Navigatable, Cacheable {

    /** TextField for entering net income */
    @FXML private TextField netIncomeField;
    /** TextField for entering fixed costs */
    @FXML private TextField fixedCostsField;
    /** TextField for entering minimum living costs */
    @FXML private TextField minLivingCostField;
    /** TextField for entering liquid assets */
    @FXML private TextField liquidAssetsField;
    /** Button to proceed to next page */
    @FXML private Button continueButton;
    /** Button to return to previous page */
    @FXML private Button backButton;

    /** Reference to the current customer */
    private static final Customer currentCustomer = Main.customer;
    /** Reference to the customer's financial information */
    private static final FinancialInformation financialInfo = currentCustomer.getFinancialInformation();
    /** Evaluator for validating financial information */
    private static final FinancialInformationEvaluator evaluator = new FinancialInformationEvaluator(financialInfo);

    /**
     * Initializes the controller by restoring previously saved data.
     * @param url The location used to resolve relative paths
     * @param resourceBundle The resource bundle containing localized data
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        evaluator.setCurrentCustomer(currentCustomer);

        restoreData();
    }

    /**
     * Handles navigation to the next page after validating financial information.
     * Performs validation and evaluation of the declared financial data.
     * Shows error messages if validation or evaluation fails.
     * @param e MouseEvent triggered by clicking the continue button
     */
    @FXML
    @SuppressWarnings("unused")
    public void nextPage(MouseEvent e) {

        try {
            cacheData();
            evaluator.validateDeclaredFinancialInfo();
            evaluator.evaluateDeclaredFinancialInfo();
        }
        catch (NumberFormatException ex) {
            showError("Fehlerhafte Eingaben", "Sie haben fehlerhafte Angaben gemacht.", ex.getMessage());
            return;
        }
        catch (ValidationException ex) {
            showConfirmation("Validierung fehlgeschlagen", "Die Validierung Ihrer finanziellen Daten ist fehlgeschlagen.", ex.getMessage());
            return;
        }
        catch (EvaluationException ex) {
            showConfirmation("Evaluierung fehlgeschlagen", "Die Evaluierung Ihrer finanziellen Daten ist fehlgeschlagen.", ex.getMessage());
            return;
        }

        loadScene(e, "customer_registration/page_5_proof_of_financial_info.fxml", "Nachweis finanzieller Angaben");
    }

    /**
     * Handles navigation to the previous page after caching current data.
     * @param e MouseEvent triggered by clicking the back button
     */
    @FXML
    @SuppressWarnings("unused")
    public void previousPage(MouseEvent e) {

        cacheData();
        loadScene(e, "customer_registration/page_3_personal_info.fxml", "Finanzielle Angaben");
    }

    /**
     * Saves the current financial data input by the user.
     * Converts text field values to integers and stores them in the financial information object.
     * @throws NumberFormatException if input values cannot be parsed as integers
     */
    public void cacheData() throws NumberFormatException {

        financialInfo.setAvgNetIncome(Integer.parseInt(netIncomeField.getText().trim()));
        financialInfo.setMonthlyFixCost(Integer.parseInt(fixedCostsField.getText().trim()));
        financialInfo.setMinCostOfLiving(Integer.parseInt(minLivingCostField.getText().trim()));
        financialInfo.setLiquidAssets(Integer.parseInt(liquidAssetsField.getText().trim()));
    }

    /**
     * Restores previously saved financial data to the text fields.
     * Called during initialization to populate fields with existing data.
     */
    public void restoreData() {

        netIncomeField.setText(String.valueOf(financialInfo.getAvgNetIncome()));
        fixedCostsField.setText(String.valueOf(financialInfo.getMonthlyFixCost()));
        minLivingCostField.setText(String.valueOf(financialInfo.getMinCostOfLiving()));
        liquidAssetsField.setText(String.valueOf(financialInfo.getLiquidAssets()));
    }

}