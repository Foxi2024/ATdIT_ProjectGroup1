package com.atdit.booking.frontend.controller;

import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;
import com.atdit.booking.backend.financialdata.processing.FinancialInformationEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Page4DeclarationFIControllerTest {
    private FinancialInformationEvaluator evaluator;
    private FinancialInformation financialInfo;

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

    @Test
    void testInsufficientLiquidAssets() {
        financialInfo.setLiquidAssets(100);
        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluateDeclaredFinancialInfo());
    }

    @Test
    void testInsufficientMonthlyMoney() {
        financialInfo.setMonthlyAvailableMoney(100);
        financialInfo.setLiquidAssets(1000);
        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluateDeclaredFinancialInfo());
    }

    @Test
    void testExtremeFinancialValues() {
        // Test mit sehr hohem Einkommen
        financialInfo.setAvgNetIncome(100000);
        assertDoesNotThrow(() -> evaluator.validateDeclaredFinancialInfo());

        // Test mit sehr hohen monatlichen Fixkosten
        financialInfo.setMonthlyFixCost(50000);
        assertThrows(IllegalArgumentException.class,
                () -> evaluator.evaluateDeclaredFinancialInfo());
    }

    @Test
    void testMonthlyAvailableMoneyCalculation() {
        financialInfo.setAvgNetIncome(3000);
        financialInfo.setMonthlyFixCost(1000);
        financialInfo.setMinCostOfLiving(800);

        // Verfügbares Geld sollte 1200 sein (3000 - 1000 - 800)
        assertEquals(1200, financialInfo.getMonthlyAvailableMoney());
    }

    @Test
    void testMinimumRequirements() {
        // Test der grundlegenden Validierung
        financialInfo.setAvgNetIncome(2000);
        financialInfo.setMonthlyFixCost(500);
        financialInfo.setMinCostOfLiving(500);
        assertDoesNotThrow(() -> evaluator.validateDeclaredFinancialInfo());

        // Test der liquiden Mittel separat
        assertThrows(IllegalArgumentException.class,
                () -> evaluator.evaluateDeclaredFinancialInfo(),
                "Sollte fehlschlagen, wenn liquide Mittel unter 20% des Reisepreises liegen");
    }

    @Test
    void testLiquidAssetsRequirement() {
        // Separate Methode für den Test der liquiden Mittel
        financialInfo.setAvgNetIncome(15000);           // Erhöht auf 15.000€
        financialInfo.setMonthlyFixCost(2000);          // Reduziert auf 2.000€
        financialInfo.setMinCostOfLiving(1500);         // Reduziert auf 1.500€
        financialInfo.setLiquidAssets(200000);          // Sehr hoher Wert für liquide Mittel
        // Verfügbares Geld = 15000 - 2000 - 1500 = 11500€

        assertDoesNotThrow(() -> evaluator.validateDeclaredFinancialInfo());
        assertDoesNotThrow(() -> evaluator.evaluateDeclaredFinancialInfo());
    }

    @Test
    void testInvalidRatios() {
        // Fixkosten höher als Einkommen
        financialInfo.setAvgNetIncome(2000);
        financialInfo.setMonthlyFixCost(2500);

        assertThrows(IllegalArgumentException.class,
                () -> evaluator.evaluateDeclaredFinancialInfo());

        // Lebenshaltungskosten höher als verfügbares Geld
        financialInfo.setMonthlyFixCost(1000);
        financialInfo.setMinCostOfLiving(1500);

        assertThrows(IllegalArgumentException.class,
                () -> evaluator.evaluateDeclaredFinancialInfo());
    }

    @Test
    void testZeroValues() {
        // Test mit Nullwert für Einkommen
        financialInfo.setAvgNetIncome(0);
        assertThrows(IllegalArgumentException.class,
                () -> evaluator.evaluateDeclaredFinancialInfo());

        // Test mit gültigem Einkommen und 0 Fixkosten
        financialInfo.setAvgNetIncome(3000);
        financialInfo.setMonthlyFixCost(0);
        assertDoesNotThrow(() -> evaluator.validateDeclaredFinancialInfo());
    }
}