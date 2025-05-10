package com.atdit.booking.financialdata;

import com.atdit.booking.Main;
import com.atdit.booking.customer.Customer;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class FinancialInformationEvaluator {

    private final double MAX_DEVIATION = 0.05;
    public static final int MAX_DOCUMENT_AGE_DAYS = 365;

    private final FinancialInformation financialInfo;
    private final Customer currentCustomer = Main.customer;

    public FinancialInformationEvaluator(FinancialInformation financialInfo){
        this.financialInfo = financialInfo;
    }

    public boolean valDeclaredFinancialInfo(int journeyPrice){
        return  this.financialInfo.getLiquidAssets() > journeyPrice
                || (this.financialInfo.getLiquidAssets() > journeyPrice * 0.3
                && financialInfo.getMonthlyAvailableMoney() > Main.MIN_MONTHLY_MONEY);
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

    public void evaluateUploads() {

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

        if(financialInfo.getSchufa().getScore() < 0.975) {
            errorMessage += "- Ihre Schufapunktzahl ist zu niedrig.\n";
            isValid = false;
        }

        if(financialInfo.getSchufa().getTotalMonthlyRate() > financialInfo.getMonthlyFixCost()) {
            errorMessage += "- Die Monatliche Rate aller Kredite ist größer als ihre monatlichen Fixkosten.\n";
            isValid = false;
        }

        if(!financialInfo.getSchufa().getFirstName().equals(currentCustomer.getFirstName()) ||
                !financialInfo.getSchufa().getLastName().equals(currentCustomer.getName())) {
            errorMessage += "- Name in Schufa information does not match your personal information\n";
            isValid = false;
        }

        if(!isValid){
            throw new IllegalArgumentException(errorMessage);
        }
    }


    public boolean validateDocumentFormat(String content, String documentType) throws IllegalArgumentException {
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

        return hasRequiredFields;
    }

    public boolean validateDocumentDate(String content) throws IllegalArgumentException{
        String[] lines = content.split("\n");

        for (String line : lines) {
            if (line.startsWith("Date Issued:")) {
                String dateStr = line.split(":")[1].trim();

                try {
                    LocalDate docDate = LocalDate.parse(dateStr);
                    LocalDate now = LocalDate.now();
                    long daysBetween = ChronoUnit.DAYS.between(docDate, now);
                    return daysBetween <= MAX_DOCUMENT_AGE_DAYS;

                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format in document: " + dateStr);
                }
            }
        }

        throw new IllegalArgumentException("No date information found in document");
    }

    public void validateDeclaredFinancialInfo() {

        String errorMessage = "Please fix the following issues:\n";
        boolean isValid = true;

            if (this.financialInfo.getAvgNetIncome() < 0) {
                errorMessage += "- Net income cannot be negative\n";
                isValid = false;
            }


            if (this.financialInfo.getMonthlyFixCost() < 0) {
                errorMessage += "- Fixed costs cannot be negative\n";
                isValid = false;
            }


            if (this.financialInfo.getMinCostOfLiving() < 0) {
                errorMessage += "- Minimum living cost cannot be negative\n";
                isValid = false;
            }


            if (this.financialInfo.getLiquidAssets() < 0) {
                errorMessage += "- Liquid assets cannot be negative\n";
                isValid = false;
            }


        if (!isValid) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public void validateUploads() {

        String errorMessage = "Please upload the following documents:\n";
        boolean isValid = true;

        if(financialInfo.getProofOfLiquidAssets() == null) {

            errorMessage += "- Proof of liquid assets\n";
            isValid = false;
        }

        if(financialInfo.getSchufa() == null) {

            errorMessage += "- Schufa information\n";
            isValid = false;
        }

        if(!isValid){
            throw new IllegalArgumentException(errorMessage);
        }

    }

}