package com.atdit.booking.backend.financialdata.processing;

import com.atdit.booking.backend.financialdata.financial_information.IncomeProof;
import com.atdit.booking.backend.financialdata.financial_information.LiquidAsset;
import com.atdit.booking.backend.financialdata.financial_information.SchufaOverview;

public class FinancialInformationParser {

    public IncomeProof parseIncomeDocument(String content) throws IllegalArgumentException {
        String[] lines = content.split("\n");
        int monthlyIncome = 0;
        String employmentType = "";
        String employer = "";
        int duration = 0;
        String dateIssued = "";

        for (String line : lines) {

            switch (line.split(":")[0].trim()) {
                case "Monthly Net Income" -> monthlyIncome = extractIntValue(line);
                case "Employer" -> employer = extractStringValue(line);
                case "Employment Type" -> employmentType = extractStringValue(line);
                case "Employment Duration" -> duration = extractIntValue(line);
                case "Date Issued" -> dateIssued = extractStringValue(line);
            }
        }

        return new IncomeProof(monthlyIncome, employer, employmentType, duration, dateIssued);
    }

    public LiquidAsset parseLiquidAssetsDocument(String content) throws IllegalArgumentException {

        System.out.println(1);

        String[] lines = content.split("\n");
        int balance = 0;
        String iban = "";
        String dateIssued = "";
        String description = "";

        for (String line : lines) {

            switch (line.split(":")[0].trim()) {
                case "Bank Account Balance" -> balance = extractIntValue(line);
                case "IBAN" -> iban = extractStringValue(line);
                case "Date Issued" -> dateIssued = extractStringValue(line);
                case "Description" -> description = extractStringValue(line);
            }
        }

        return new LiquidAsset(iban, description, balance,  dateIssued);
    }

    public SchufaOverview parseSchufaDocument(String content) {

        SchufaOverview schufaOverview = new SchufaOverview();
        String[] lines = content.split("\n");

        for (String line : lines) {

            switch (line.split(":")[0].trim()) {
                case "First Name" -> schufaOverview.setFirstName(extractStringValue(line));
                case "Last Name" -> schufaOverview.setLastName(extractStringValue(line));
                case "Schufa Score" -> schufaOverview.setScore(extractDoubleValue(line));
                case "Total Credits" -> schufaOverview.setTotalCredits(extractIntValue(line));
                case "Total Credit Sum" -> schufaOverview.setTotalCreditSum(extractIntValue(line));
                case "Total Amount Payed" -> schufaOverview.setTotalAmountPayed(extractIntValue(line));
                case "Total Amount Owed" -> schufaOverview.setTotalAmountOwed(extractIntValue(line));
                case "Total Monthly Rate" -> schufaOverview.setTotalMonthlyRate(extractIntValue(line));
                case "Date Issued" -> schufaOverview.setDateIssued(extractStringValue(line));
            }
        }

        return schufaOverview;
    }

    private int extractIntValue(String line) {

        try {
            String value = line.split(":")[1].trim();
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in line: " + line);
        }
    }

    private double extractDoubleValue(String line) {

        try {
            String value = line.split(":")[1].trim();
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid decimal format in line: " + line);
        }
    }

    private String extractStringValue(String line) {
        return line.split(":")[1].trim();
    }

}
