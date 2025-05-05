package com.atdit.booking.financialdata;

import com.atdit.booking.Main;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class FinancialInformationEvaluator {

    private final double MAX_DEVIATION = 0.10;
    public static final int MAX_DOCUMENT_AGE_DAYS = 365;

    private final FinancialInformation financialInfo;

    public FinancialInformationEvaluator(FinancialInformation financialInfo){
        this.financialInfo = financialInfo;
    }

    public boolean valDeclaredFinancialInfo(int journeyPrice){
        return this.financialInfo.getLiquidAssets() > journeyPrice * 0.2 &&
                financialInfo.getMonthlyAvailableMoney() > Main.MIN_MONTHLY_MONEY;
    }

    public boolean evaluateIncome() {

        return ((double) (financialInfo.getProofOfIncome().monthlyNetIncome() / financialInfo.getAvgNetIncome()) > (1-MAX_DEVIATION));
    }

    private boolean evaluateLiquidAssets() {
        System.out.println(financialInfo.getProofOfLiquidAssets().balance());
        System.out.println();
        System.out.println(financialInfo.getLiquidAssets());

        return ((double) (financialInfo.getProofOfLiquidAssets().balance() / financialInfo.getLiquidAssets()) > (1-MAX_DEVIATION));

    }

    public void evaluateUploads() {

        String errorMessage = "Please fix the following issues:\n";
        boolean isValid = true;

        if(!evaluateLiquidAssets()){
            errorMessage += "- Declared liquid assets differ significantly from proof of assets\n";
            isValid = false;

        }

        if(!evaluateIncome()){
            errorMessage += "- Declared income differs significantly from proof of income\n";
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
                hasRequiredFields = lines.length == 7
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

    public boolean validateDocumentDate(String content) {
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