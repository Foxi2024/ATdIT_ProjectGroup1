package com.atdit.booking.financialdata;

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

    public void validateIncome() {

        if ((double) financialInfo.getProofOfIncome().monthlyNetIncome() / financialInfo.getAvgNetIncome() < 1-MAX_DEVIATION) {
            throw new IllegalArgumentException(String.format(
                    "Declared monthly income (" +
                            financialInfo.getProofOfIncome().monthlyNetIncome()  +
                            "€) differs significantly from proof of income (" +
                            financialInfo.getAvgNetIncome() +
                            "€). "
            ));
        }
    }

    public void validateLiquidAssets() throws IllegalArgumentException {

        if ((double) financialInfo.getProofOfLiquidAssets().balance() / financialInfo.getLiquidAssets() < 1-MAX_DEVIATION) {
            throw new IllegalArgumentException(
                    "Declared liquid assets (" +
                            financialInfo.getLiquidAssets() +
                            "€) differs significantly from proof of assets (" +
                            financialInfo.getProofOfLiquidAssets().balance() +
                            "€). "
            );
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


}