package com.atdit.booking.frontend.controllers.customer_registration;

import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;
import com.atdit.booking.backend.financialdata.processing.FinancialInformationEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for validating financial information declarations in Page 4 of the booking process.
 * Tests various scenarios and edge cases for financial data validation.
 */
class Page4DeclarationFIControllerTest {
    private FinancialInformationEvaluator evaluator;
    private FinancialInformation financialInfo;

    /**
     * Sets up the test environment before each test.
     * The price for a journey will be 1,000,000 €.
     * Initializes a FinancialInformation object with default test values and creates an evaluator.
     */
    @BeforeEach
    void setUp() {
        financialInfo = new FinancialInformation();
        financialInfo.setAvgNetIncome(3000);
        financialInfo.setMonthlyFixCost(1000);
        financialInfo.setMinCostOfLiving(800);
        financialInfo.setLiquidAssets(10000);
        financialInfo.setMonthlyAvailableMoney(1200);

        evaluator = new FinancialInformationEvaluator(financialInfo);
    }

    /**
     * Tests validation of negative financial values.
     * Verifies that appropriate exceptions are thrown for invalid negative inputs.
     */
    @Test
    void testNegativeFinancialValues() {
        assertThrows(IllegalArgumentException.class, () -> financialInfo.setAvgNetIncome(-1000));

        financialInfo.setAvgNetIncome(3000);
        financialInfo.setMonthlyFixCost(-500);
        assertThrows(ValidationException.class, () -> evaluator.validateDeclaredFinancialInfo());

        financialInfo.setMonthlyFixCost(1000);
        financialInfo.setLiquidAssets(-5000);
        assertThrows(ValidationException.class, () -> evaluator.validateDeclaredFinancialInfo());
    }

    /**
     * Tests scenarios with insufficient liquid assets.
     * Verifies that appropriate exceptions are thrown when liquid assets are too low.
     */
    @Test
    void testInsufficientLiquidAssets() {
        financialInfo.setLiquidAssets(100);
        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluateDeclaredFinancialInfo());
    }

    /**
     * Tests scenarios with insufficient monthly available money.
     * Verifies that appropriate exceptions are thrown when monthly available money is too low.
     */
    @Test
    void testInsufficientMonthlyMoney() {
        financialInfo.setMonthlyAvailableMoney(100);
        financialInfo.setLiquidAssets(1000);
        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluateDeclaredFinancialInfo());
    }

    /**
     * Tests validation of extreme financial values.
     * Verifies handling of very high income and monthly fixed costs.
     */
    @Test
    void testExtremeFinancialValues() {
        // Test with very high income
        financialInfo.setAvgNetIncome(100000);
        assertDoesNotThrow(() -> evaluator.validateDeclaredFinancialInfo());

        // Test with very high monthly fixed costs
        financialInfo.setMonthlyFixCost(50000);
        assertThrows(IllegalArgumentException.class,
                () -> evaluator.evaluateDeclaredFinancialInfo());
    }

    /**
     * Tests the calculation of monthly available money.
     * Verifies that the calculation (income - fixed costs - minimum cost of living) is correct.
     */
    @Test
    void testMonthlyAvailableMoneyCalculation() {
        financialInfo.setAvgNetIncome(3000);
        financialInfo.setMonthlyFixCost(1000);
        financialInfo.setMinCostOfLiving(800);

        // Available money should be 1200 (3000 - 1000 - 800)
        assertEquals(1200, financialInfo.getMonthlyAvailableMoney());
    }

    /**
     * Tests the minimum requirements for financial validation.
     * Verifies basic validation and liquid assets requirements.
     */
    @Test
    void testMinimumRequirements() {
        // Test basic validation
        financialInfo.setAvgNetIncome(2000);
        financialInfo.setMonthlyFixCost(500);
        financialInfo.setMinCostOfLiving(500);
        assertDoesNotThrow(() -> evaluator.validateDeclaredFinancialInfo());

        // Test liquid assets separately
        assertThrows(IllegalArgumentException.class,
                () -> evaluator.evaluateDeclaredFinancialInfo(),
                "Should fail if liquid assets are below 20% of travel price");
    }

    /**
     * Tests the requirements for liquid assets with high values.
     * Verifies that validation passes with sufficient financial means.
     */
    @Test
    void testLiquidAssetsRequirement() {
        // Separate method for testing liquid assets
        financialInfo.setAvgNetIncome(15000);           // Increased to 15,000€
        financialInfo.setMonthlyFixCost(2000);          // Reduced to 2,000€
        financialInfo.setMinCostOfLiving(1500);         // Reduced to 1,500€
        financialInfo.setLiquidAssets(200000);          // Very high value for liquid assets
        // Available money = 15000 - 2000 - 1500 = 11500€

        assertDoesNotThrow(() -> evaluator.validateDeclaredFinancialInfo());
        assertDoesNotThrow(() -> evaluator.evaluateDeclaredFinancialInfo());
    }

    /**
     * Tests invalid financial ratios.
     * Verifies that appropriate exceptions are thrown when financial ratios are invalid.
     */
    @Test
    void testInvalidRatios() {
        // Fixed costs higher than income
        financialInfo.setAvgNetIncome(2000);
        financialInfo.setMonthlyFixCost(2500);

        assertThrows(IllegalArgumentException.class,
                () -> evaluator.evaluateDeclaredFinancialInfo());

        // Cost of living higher than available money
        financialInfo.setMonthlyFixCost(1000);
        financialInfo.setMinCostOfLiving(1500);

        assertThrows(IllegalArgumentException.class,
                () -> evaluator.evaluateDeclaredFinancialInfo());
    }

    /**
     * Tests validation with zero values.
     * Verifies handling of zero values for income and fixed costs.
     */
    @Test
    void testZeroValues() {
        // Test with zero value for income
        financialInfo.setAvgNetIncome(0);
        assertThrows(IllegalArgumentException.class,
                () -> evaluator.evaluateDeclaredFinancialInfo());

        // Test with valid income and zero fixed costs
        financialInfo.setAvgNetIncome(3000);
        financialInfo.setMonthlyFixCost(0);
        assertDoesNotThrow(() -> evaluator.validateDeclaredFinancialInfo());
    }
}