package com.atdit.booking.backend.financialdata.processing;

import com.atdit.booking.backend.exceptions.EvaluationException;
import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.contracts.FinancingContract;
import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * This class evaluates financial information for a financing contract.
 * It performs various validations and evaluations on the customer's financial data
 * including income verification, liquid assets assessment, and document validation.
 */
public class FinancialInformationEvaluator {

    /** Maximum allowed deviation between declared and actual financial values (5%) */
    private final double MAX_DEVIATION = 0.05;

    /** Maximum age of submitted documents in days (1 year) */
    public static final int MAX_DOCUMENT_AGE_DAYS = 365;

    /** Details of the financing contract */
    private final FinancingContract journeyDetails = new FinancingContract();

    /** Minimum required monthly available money */
    private final double MIN_MONTHLY_MONEY;

    /** Financial information object containing all relevant customer data */
    private final FinancialInformation financialInfo;

    /** Current customer object */
    private Customer currentCustomer;

    /**
     * Constructor initializes the evaluator with financial information
     * and sets up basic contract parameters.
     *
     * @param financialInfo The financial information to be evaluated
     */
    public FinancialInformationEvaluator(FinancialInformation financialInfo){
        this.financialInfo = financialInfo;
        this.journeyDetails.setMonths(60);
        MIN_MONTHLY_MONEY = journeyDetails.getMonthlyPayment() * 0.8;
    }
    /**
     * Sets the current customer for financial evaluation.
     * The current customer is required for validating personal information against documents,
     * particularly for Schufa score verification where names must match.
     *
     * @param currentCustomer The Customer object representing the current user whose financial
     *                       information is being evaluated
     */
    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    /**
     * Evaluates the declared financial information against basic criteria.
     * Checks if liquid assets are sufficient for down payment and if monthly available money meets minimum requirements.
     *
     * @throws IllegalArgumentException if the financial criteria are not met
     */
    public void evaluateDeclaredFinancialInfo() throws IllegalArgumentException {

        // If liquid assets are more than 120% of the total amount, no further checks are needed for declared info
        // This implies the customer has significant funds, potentially covering the entire amount
        if(financialInfo.getLiquidAssets() > 1.2 * journeyDetails.TOTAL_AMOUNT){
            return;
        }

        // Check if liquid assets are sufficient for the down payment (at least 20% of the travel price)
        if(this.financialInfo.getLiquidAssets() < journeyDetails.getDownPayment()){
            throw new IllegalArgumentException("Ihre liquiden Mittel sind zu niedrig. Sie benötigen mindestens 20% des Reisepreises.");
        }

        // Check if the monthly available money meets the minimum requirement
        // MIN_MONTHLY_MONEY is calculated as 80% of the monthly payment
        if(financialInfo.getMonthlyAvailableMoney() < MIN_MONTHLY_MONEY){
            throw new IllegalArgumentException("Ihr monatliches verfügbares Geld ist zu niedrig. Sie benötigen mindestens " + MIN_MONTHLY_MONEY +"€.");
        }
    }

    /**
     * Evaluates if the declared income matches the proof of income within allowed deviation.
     *
     * @return true if income verification passes or if no proof is provided
     */
    public boolean evaluateIncome() {

        if (financialInfo.getProofOfIncome() == null) {
            return true;
        }

        double monthlyNeIncome = financialInfo.getProofOfIncome().monthlyNetIncome();

        return (monthlyNeIncome / financialInfo.getAvgNetIncome()) > (1-MAX_DEVIATION);
    }

    /**
     * Evaluates if the declared liquid assets match the proof of assets within allowed deviation.
     *
     * @return true if liquid assets verification passes
     */
    private boolean evaluateLiquidAssets() {

        double accountBalance = financialInfo.getProofOfLiquidAssets().balance();

        return (accountBalance / financialInfo.getLiquidAssets()) > (1-MAX_DEVIATION);

    }

    /**
     * Evaluates all uploaded documents including Schufa score, income verification,
     * and liquid assets verification.
     *
     * @throws EvaluationException if any evaluation criteria are not met
     */
    public void evaluateUploads() throws EvaluationException {

        String errorMessage = "Folgende Probleme sind aufgetaucht:\n";
        boolean isValid = true;

        if(!evaluateLiquidAssets()) {
            errorMessage += "- Ihre angegebenen liquiden Mittel weichen zu stark von Ihren tatsächlichen liquiden Mitteln ab.\n";
            isValid = false;
        }

        if(!evaluateIncome()) {
            errorMessage += "- Ihre angegebenes Einkommen weicht zu stark von Ihrem tatsächlichen Einkommen ab.\n";
            isValid = false;
        }

        // Check if the Schufa score meets the minimum requirement (95%)
        if(financialInfo.getSchufa().getScore() < 0.95) {
            errorMessage += "- Ihre Schufapunktzahl ist zu niedrig.\n";
            isValid = false;
        }

        // Check if the total monthly rate of all credits exceeds the customer's monthly fixed costs
        // This could indicate an unsustainable debt burden
        if(financialInfo.getSchufa().getTotalMonthlyRate() > financialInfo.getMonthlyFixCost()) {
            errorMessage += "- Die Monatliche Rate aller Kredite ist größer als ihre monatlichen Fixkosten.\n";
            isValid = false;
        }

        // Verify that the first and last names on the Schufa document match the current customer's details
        if(!financialInfo.getSchufa().getFirstName().equals(currentCustomer.getFirstName()) ||
                !financialInfo.getSchufa().getLastName().equals(currentCustomer.getLastName())) {
            errorMessage += "- Name im Schufa Dokument stimmt nicht mit Ihrem Namen überein\n";
            isValid = false;
        }

        if(!isValid){
            throw new EvaluationException(errorMessage);
        }
    }

    /**
     * Validates the format of uploaded documents based on document type.
     * Checks for required fields in income proof, liquid assets proof, and Schufa document.
     *
     * @param content The document content to validate
     * @param documentType The type of document ("income", "liquidAssets", or "schufa")
     * @throws ValidationException if the document format is invalid
     */
    public void validateDocumentFormat(String content, String documentType) {
        String[] lines = content.split("\n");
        boolean hasRequiredFields = switch (documentType) {

            // Validate income document: must have 5 lines and contain specific keywords
            case "income" -> lines.length == 5
                    && content.contains("Monthly Net Income:")
                    && content.contains("Employment Type:")
                    && content.contains("Employer:")
                    && content.contains("Employment Duration:")
                    && content.contains("Date Issued:");

            // Validate liquid assets document: must have 4 lines and contain specific keywords
            case "liquidAssets" -> lines.length == 4
                    && content.contains("Bank Account Balance:")
                    && content.contains("IBAN:")
                    && content.contains("Description:")
                    && content.contains("Date Issued:");

            // Validate Schufa document: must have 9 lines and contain specific keywords
            case "schufa" -> lines.length == 9
                    && content.contains("First Name:")
                    && content.contains("Last Name:")
                    && content.contains("Schufa Score:")
                    && content.contains("Total Credits:")
                    && content.contains("Total Credit Sum:")
                    && content.contains("Total Amount Payed:")
                    && content.contains("Total Amount Owed:")
                    && content.contains("Total Monthly Rate:")
                    && content.contains("Date Issued:");

            default -> false; // Unknown document type
        };

        if(!hasRequiredFields){
            throw new ValidationException("Invalides Format");
        }

    }

    /**
     * Validates the issue date of documents to ensure they are not too old.
     *
     * @param content The document content containing the date
     * @throws ValidationException if the date format is invalid or no date is found
     * @throws EvaluationException if the document is older than the maximum allowed age
     */
    public void validateDocumentDate(String content) throws ValidationException, EvaluationException{
        String[] lines = content.split("\n");

        for (String line : lines) {
            if (line.startsWith("Date Issued:")) {
                String dateStr = line.split(":")[1].trim();

                try {
                    LocalDate docDate = LocalDate.parse(dateStr);
                    LocalDate now = LocalDate.now();
                    long daysBetween = ChronoUnit.DAYS.between(docDate, now);
                    if(daysBetween > MAX_DOCUMENT_AGE_DAYS) {
                        throw new EvaluationException("Dokument zu alt (maximal " + FinancialInformationEvaluator.MAX_DOCUMENT_AGE_DAYS + " Tage alt)");
                    }
                    return;

                } catch (DateTimeParseException e) {
                    throw new ValidationException("Invalides Datumsformat: " + dateStr);
                }
            }
        }

        throw new ValidationException("Kein Datumsfeld im Dokument gefunden");
    }

    /**
     * Validates the declared financial information for basic plausibility.
     * Checks if values are non-negative.
     *
     * @throws ValidationException if any declared values are negative
     */
    public void validateDeclaredFinancialInfo() throws ValidationException{

        String errorMessage = "Folgende Probleme sind aufgetaucht:\n";
        boolean isValid = true;

            // Validate that average net income is not negative
            if (this.financialInfo.getAvgNetIncome() < 0) {
                errorMessage += "- Einkommen darf nicht negativ sein\n";
                isValid = false;
            }

            // Validate that monthly fixed costs are not negative
            if (this.financialInfo.getMonthlyFixCost() < 0) {
                errorMessage += "- Fixkosten dürfen nicht negativ sein\n";
                isValid = false;
            }

            // Validate that minimum cost of living is not negative
            if (this.financialInfo.getMinCostOfLiving() < 0) {
                errorMessage += "- Min. Lebenserhaltungskosten dürfen nicht negativ sein\n";
                isValid = false;
            }

            // Validate that liquid assets are not negative
            if (this.financialInfo.getLiquidAssets() < 0) {
                errorMessage += "- Liquide Mittel dürfen nicht negativ sein\n";
                isValid = false;
            }


        if (!isValid) {
            throw new ValidationException(errorMessage);
        }
    }

    /**
     * Validates that all required documents have been uploaded.
     *
     * @throws ValidationException if any required documents are missing
     */
    public void validateUploads() {

        String errorMessage = "Bitte laden sie folgende Dokumente hoch:\n";
        boolean isValid = true;

        // Check if proof of liquid assets has been uploaded
        if(financialInfo.getProofOfLiquidAssets() == null) {

            errorMessage += "- Kontoauszug\n";
            isValid = false;
        }

        // Check if Schufa document has been uploaded
        if(financialInfo.getSchufa() == null) {

            errorMessage += "- Schufaunterlagen\n";
            isValid = false;
        }

        if(!isValid){
            throw new ValidationException(errorMessage);
        }

    }

}