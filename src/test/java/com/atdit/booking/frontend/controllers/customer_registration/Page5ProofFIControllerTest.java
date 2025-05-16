package com.atdit.booking.frontend.controllers.customer_registration;

import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;
import com.atdit.booking.backend.financialdata.financial_information.IncomeProof;
import com.atdit.booking.backend.financialdata.financial_information.LiquidAsset;
import com.atdit.booking.backend.financialdata.financial_information.SchufaOverview;
import com.atdit.booking.backend.financialdata.processing.FinancialInformationEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Page5ProofFIControllerTest {
    private FinancialInformationEvaluator evaluator;
    private FinancialInformation financialInfo;
    private IncomeProof proofOfIncome;
    private LiquidAsset proofOfLiquidAssets;
    private SchufaOverview schufa;
    private Customer customer;

    /**
     * Sets up the test environment before each test.
     * Initializes financial information, customer data, and required documents.
     */
    @BeforeEach
    void setUp() {
        // Customer und FinancialInfo zuerst erstellen
        financialInfo = new FinancialInformation();
        financialInfo.setAvgNetIncome(3000);
        financialInfo.setMonthlyFixCost(1000);
        financialInfo.setMinCostOfLiving(800);
        financialInfo.setLiquidAssets(10000);
        financialInfo.setMonthlyAvailableMoney(1200);

        evaluator = new FinancialInformationEvaluator(financialInfo);

        proofOfLiquidAssets = new LiquidAsset(
                "DE89370400440532013000",
                "Gehaltskonto",
                9800,
                "2024-03-20"
        );

        schufa = new SchufaOverview();
        schufa.setScore(96);
        schufa.setFirstName("Max");
        schufa.setLastName("Mustermann");
        schufa.setTotalMonthlyRate(800);
        schufa.setDateIssued("2024-03-20");
        schufa.setTotalCredits(1);
        schufa.setTotalCreditSum(50000);
        schufa.setTotalAmountPayed(10000);
        schufa.setTotalAmountOwed(40000);

        proofOfIncome = new IncomeProof(
                2900,
                "ACME Corp",
                "Vollzeit",
                24,
                "2024-03-15"
        );

        financialInfo.setProofOfIncome(proofOfIncome);
        financialInfo.setProofOfLiquidAssets(proofOfLiquidAssets);
        financialInfo.setSchufa(schufa);

        Customer customer = new Customer();
        customer.setFirstName("Max");
        customer.setLastName("Mustermann");
        evaluator.setCurrentCustomer(customer);
    }

    /**
     * Tests if income evaluation fails when the declared income deviates too much from the proof.
     */
    @Test
    void testIncomeDeviationTooHigh() {
        proofOfIncome = new IncomeProof(
                2000,
                "ACME Corp",
                "Vollzeit",
                24,
                "2024-03-15"
        );
        financialInfo.setProofOfIncome(proofOfIncome);
        assertFalse(evaluator.evaluateIncome());
    }

    /**
     * Tests if evaluation throws an exception when liquid assets deviate too much from the declared amount.
     */
    @Test
    void testLiquidAssetsDeviationTooHigh() {
        proofOfLiquidAssets = new LiquidAsset(
                "DE89370400440532013000",
                "Gehaltskonto",
                8000,
                "2024-03-20"
        );
        financialInfo.setProofOfLiquidAssets(proofOfLiquidAssets);
        assertThrows(ValidationException.class, () -> evaluator.evaluateUploads());
    }

    /**
     * Tests if validation fails when required documents are missing.
     */
    @Test
    void testMissingDocuments() {
        financialInfo.setProofOfLiquidAssets(null);
        assertThrows(ValidationException.class, () -> evaluator.validateUploads());

        financialInfo.setSchufa(null);
        assertThrows(ValidationException.class, () -> evaluator.validateUploads());
    }

    /**
     * Tests if evaluation fails when SCHUFA name doesn't match customer name.
     */
    @Test
    void testNameMismatchInSchufa() {
        schufa = new SchufaOverview();
        schufa.setScore(96);
        schufa.setFirstName("Wrong");
        schufa.setLastName("Name");
        schufa.setTotalMonthlyRate(800);
        schufa.setDateIssued("2024-03-20");
        schufa.setTotalCredits(1);
        schufa.setTotalCreditSum(50000);
        schufa.setTotalAmountPayed(10000);
        schufa.setTotalAmountOwed(40000);
        financialInfo.setSchufa(schufa);
        assertThrows(ValidationException.class, () -> evaluator.evaluateUploads());
    }

    /**
     * Tests if evaluation fails when SCHUFA shows too high monthly rates.
     */
    @Test
    void testTotalMonthlyRateTooHigh() {
        schufa = new SchufaOverview();
        schufa.setScore(96);
        schufa.setFirstName("Max");
        schufa.setLastName("Mustermann");
        schufa.setTotalMonthlyRate(1200);
        schufa.setDateIssued("2024-03-20");
        schufa.setTotalCredits(1);
        schufa.setTotalCreditSum(50000);
        schufa.setTotalAmountPayed(10000);
        schufa.setTotalAmountOwed(40000);
        financialInfo.setSchufa(schufa);
        assertThrows(ValidationException.class, () -> evaluator.evaluateUploads());
    }

    /**
     * Tests if validation fails when document format is invalid.
     */
    @Test
    void testInvalidDocumentFormat() {
        String invalidIncomeDoc = """
            Monthly Net Income: 3000
            Employment Type: Vollzeit
            Employer: ACME Corp
            Date Issued: 2024-03-15
            """;  // Fehlendes Employment Duration Feld

        assertThrows(ValidationException.class,
                () -> evaluator.validateDocumentFormat(invalidIncomeDoc, "income"));
    }

    /**
     * Tests if evaluation fails when document is too old.
     */
    @Test
    void testDocumentTooOld() {
        String oldDocument = """
            Monthly Net Income: 3000
            Employment Type: Vollzeit
            Employer: ACME Corp
            Employment Duration: 24
            Date Issued: 2023-01-01
            """;

        assertThrows(ValidationException.class,
                () -> evaluator.validateDocumentDate(oldDocument));
    }

    /**
     * Tests if validation fails when date format is invalid.
     */
    @Test
    void testInvalidDateFormat() {
        String invalidDateDoc = """
            Monthly Net Income: 3000
            Employment Type: Vollzeit
            Employer: ACME Corp
            Employment Duration: 24
            Date Issued: 01.01.2024
            """;

        assertThrows(ValidationException.class,
                () -> evaluator.validateDocumentDate(invalidDateDoc));
    }

    /**
     * Tests if validation fails when financial values are negative.
     */
    @Test
    void testNegativeFinancialValues() {
        try {
            java.lang.reflect.Field incomeField = FinancialInformation.class.getDeclaredField("avgNetIncome");
            incomeField.setAccessible(true);
            incomeField.set(financialInfo, -1000);
            assertThrows(ValidationException.class, () -> evaluator.validateDeclaredFinancialInfo());

            incomeField.set(financialInfo, 3000);
            java.lang.reflect.Field fixCostField = FinancialInformation.class.getDeclaredField("monthlyFixCost");
            fixCostField.setAccessible(true);
            fixCostField.set(financialInfo, -500);
            assertThrows(ValidationException.class, () -> evaluator.validateDeclaredFinancialInfo());

            fixCostField.set(financialInfo, 1000);
            java.lang.reflect.Field minCostField = FinancialInformation.class.getDeclaredField("minCostOfLiving");
            minCostField.setAccessible(true);
            minCostField.set(financialInfo, -200);
            assertThrows(ValidationException.class, () -> evaluator.validateDeclaredFinancialInfo());

            minCostField.set(financialInfo, 800);
            java.lang.reflect.Field assetsField = FinancialInformation.class.getDeclaredField("liquidAssets");
            assetsField.setAccessible(true);
            assetsField.set(financialInfo, -5000);
            assertThrows(ValidationException.class, () -> evaluator.validateDeclaredFinancialInfo());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests if validation fails when document date is missing.
     */
    @Test
    void testMissingDateInDocument() {
        String noDateDoc = """
            Monthly Net Income: 3000
            Employment Type: Vollzeit
            Employer: ACME Corp
            Employment Duration: 24
            """;

        assertThrows(ValidationException.class,
                () -> evaluator.validateDocumentDate(noDateDoc));
    }

    /**
     * Tests if income evaluation succeeds with valid income proof.
     */
    @Test
    void testValidIncome() {
        proofOfIncome = new IncomeProof(
                2900,  // Nur leichte Abweichung von 3000
                "ACME Corp",
                "Vollzeit",
                24,
                "2024-03-15"
        );
        financialInfo.setProofOfIncome(proofOfIncome);
        assertTrue(evaluator.evaluateIncome());
    }
}