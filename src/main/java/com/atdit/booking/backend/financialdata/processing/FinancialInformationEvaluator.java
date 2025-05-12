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

public class FinancialInformationEvaluator {

    private final double MAX_DEVIATION = 0.05;
    public static final int MAX_DOCUMENT_AGE_DAYS = 365;
    private final FinancingContract journeyDetails = new FinancingContract();
    private final double MIN_MONTHLY_MONEY;


    private final FinancialInformation financialInfo;
    private final Customer currentCustomer = Main.customer;

    public FinancialInformationEvaluator(FinancialInformation financialInfo){
        this.financialInfo = financialInfo;
        this.journeyDetails.setMonths(60);
        MIN_MONTHLY_MONEY = journeyDetails.getMonthlyPayment() * 0.8;
    }

    public void evaluateDeclaredFinancialInfo() throws IllegalArgumentException {

        if(financialInfo.getLiquidAssets() > 1.2 * journeyDetails.TOTAL_AMOUNT){
            return;
        }

        if(this.financialInfo.getLiquidAssets() < journeyDetails.getDownPayment()){
            throw new IllegalArgumentException("Ihre liquiden Mittel sind zu niedrig. Sie benötigen mindestens 20% des Reisepreises.");
        }

        if(financialInfo.getMonthlyAvailableMoney() < MIN_MONTHLY_MONEY && financialInfo.getLiquidAssets() < 0.3 * journeyDetails.TOTAL_AMOUNT){
            throw new IllegalArgumentException("Ihr monatliches verfügbares Geld ist zu niedrig. Sie benötigen mindestens " + MIN_MONTHLY_MONEY +"€.");
        }
    }

    public boolean evaluateIncome() {

        if (financialInfo.getProofOfIncome() == null) {
            return true;
        }

        return ((double) (financialInfo.getProofOfIncome().monthlyNetIncome() / financialInfo.getAvgNetIncome()) > (1-MAX_DEVIATION));
    }

    private boolean evaluateLiquidAssets() {

        return ((double) (financialInfo.getProofOfLiquidAssets().balance() / financialInfo.getLiquidAssets()) > (1-MAX_DEVIATION));

    }



    public void evaluateUploads() throws EvaluationException{

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

    public void validateDocumentDate(String content) throws ValidationException, EvaluationException{
        String[] lines = content.split("\n");

        for (String line : lines) {
            if (line.startsWith("Date Issued:")) {
                String dateStr = line.split(":")[1].trim();

                try {
                    LocalDate docDate = LocalDate.parse(dateStr);
                    LocalDate now = LocalDate.now();
                    long daysBetween = ChronoUnit.DAYS.between(docDate, now);
                    if(daysBetween <= MAX_DOCUMENT_AGE_DAYS) {
                        throw new EvaluationException("Dokument zu alt (maximal " + FinancialInformationEvaluator.MAX_DOCUMENT_AGE_DAYS + " Tage alt)");
                    }

                } catch (DateTimeParseException e) {
                    throw new ValidationException("Invalides Datumsformat: " + dateStr);
                }
            }
        }

        throw new ValidationException("Kein Datumsfeld im Dokument gefunden");
    }

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