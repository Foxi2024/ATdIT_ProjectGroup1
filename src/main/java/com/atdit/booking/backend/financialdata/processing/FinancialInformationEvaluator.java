package com.atdit.booking.backend.financialdata.processing;

import com.atdit.booking.backend.exceptions.EvaluationException;
import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.contracts.FinancingContract;
import com.atdit.booking.Main;
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
    private final Customer currentCustomer = Main.customer;

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
     * Evaluates the declared financial information against basic criteria.
     * Checks if liquid assets are sufficient for down payment and if monthly available money meets minimum requirements.
     *
     * @throws IllegalArgumentException if the financial criteria are not met
     */
    public void evaluateDeclaredFinancialInfo() throws IllegalArgumentException {

        if(financialInfo.getLiquidAssets() > 1.2 * journeyDetails.TOTAL_AMOUNT){
            return;
        }

        if(this.financialInfo.getLiquidAssets() < journeyDetails.getDownPayment()){
            throw new IllegalArgumentException("Ihre liquiden Mittel sind zu niedrig. Sie benötigen mindestens 20% des Reisepreises.");
        }

        if(financialInfo.getMonthlyAvailableMoney() < MIN_MONTHLY_MONEY
                && financialInfo.getLiquidAssets() < 0.3 * journeyDetails.TOTAL_AMOUNT){
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

        if(financialInfo.getSchufa().getScore() < 0.95) {
            errorMessage += "- Ihre Schufapunktzahl ist zu niedrig.\n";
            isValid = false;
        }

        if(financialInfo.getSchufa().getTotalMonthlyRate() > financialInfo.getMonthlyFixCost()) {
            errorMessage += "- Die Monatliche Rate aller Kredite ist größer als ihre monatlichen Fixkosten.\n";
            isValid = false;
        }

        if(!financialInfo.getSchufa().getFirstName().equals(currentCustomer.getFirstName()) ||
                !financialInfo.getSchufa().getLastName().equals(currentCustomer.getName())) {
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
        boolean hasRequiredFields = false;

        switch(documentType) {

            case "income":
                hasRequiredFields = lines.length == 5
                        && content.contains("Monthly Net Income:")
                        && content.contains("Employment Type:")
                        && content.contains("Employer:")
                        && content.contains("Employment Duration:")
                        && content.contains("Date Issued:");
                break;

            case "liquidAssets":
                hasRequiredFields = lines.length == 4
                        && content.contains("Bank Account Balance:")
                        && content.contains("IBAN:")
                        && content.contains("Description:")
                        && content.contains("Date Issued:");
                break;

            case "schufa":
                hasRequiredFields = lines.length == 9
                        && content.contains("First Name:")
                        && content.contains("Last Name:")
                        && content.contains("Schufa Score:")
                        && content.contains("Total Credits:")
                        && content.contains("Total Credit Sum:")
                        && content.contains("Total Amount Payed:")
                        && content.contains("Total Amount Owed:")
                        && content.contains("Total Monthly Rate:")
                        && content.contains("Date Issued:");

                break;
        }

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

            if (this.financialInfo.getAvgNetIncome() < 0) {
                errorMessage += "- Einkommen darf nicht negativ sein\n";
                isValid = false;
            }


            if (this.financialInfo.getMonthlyFixCost() < 0) {
                errorMessage += "- Fixkosten dürfen nicht negativ sein\n";
                isValid = false;
            }


            if (this.financialInfo.getMinCostOfLiving() < 0) {
                errorMessage += "- Min. Lebenserhaltungskosten dürfen nicht negativ sein\n";
                isValid = false;
            }


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

        if(financialInfo.getProofOfLiquidAssets() == null) {

            errorMessage += "- Kontoauszug\n";
            isValid = false;
        }

        if(financialInfo.getSchufa() == null) {

            errorMessage += "- Schufaunterlagen\n";
            isValid = false;
        }

        if(!isValid){
            throw new ValidationException(errorMessage);
        }

    }

}