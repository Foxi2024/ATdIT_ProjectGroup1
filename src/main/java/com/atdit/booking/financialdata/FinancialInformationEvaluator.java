package com.atdit.booking.financialdata;

public class FinancialInformationEvaluator {

    public IncomeProof parseIncomeDocument(String content) {
        String[] lines = content.split("\n");
        int monthlyIncome = 0;
        String employmentType = "";
        String employer = "";
        int duration = 0;

        for (String line : lines) {

            switch (line.split(":")[0]) {
                case "Monthly Net Income:" -> monthlyIncome = extractIntValue(line);
                case "Employment Type:" -> employmentType = extractStringValue(line);
                case "Employer:" -> employer = extractStringValue(line);
                case "Employment Duration:" -> duration = extractIntValue(line);
            }
        }

        return new IncomeProof(monthlyIncome, employmentType, employer, duration);
    }

    public LiquidAsset parseLiquidAssetsDocument(String content) {

        String[] lines = content.split("\n");
        int balance = 0;
        String iban = "";
        String dateIssued = "";
        String description = "";

        for (String line : lines) {
            switch (line.split(":")[0]) {
                case "Bank Account Balance:" -> balance = extractIntValue(line);
                case "IBAN:" -> iban = extractStringValue(line);
                case "Date Issued:" -> dateIssued = extractStringValue(line);
                case "Description:" -> description = extractStringValue(line);
            }
        }

        return new LiquidAsset(iban, description, balance,  dateIssued);
    }

    public SchufaOverview parseSchufaDocument(String content) {

        SchufaOverview schufaOverview = new SchufaOverview();
        String[] lines = content.split("\n");
        double score = 0;
        String dateIssued = "";
        int totalCredits = 0;
        int totalCreditSum = 0;
        int totalAmountPayed = 0;
        int totalAmountOwed = 0;
        int totalMonthlyRate = 0;

        for (String line : lines) {
            switch (line.split(":")[0]) {
                case "Schufa Score:" -> schufaOverview.setScore(extractDoubleValue(line));
                case "Total Credits:" -> schufaOverview.setTotalCredits(extractIntValue(line));
                case "Total Credit Sum:" -> schufaOverview.setTotalCreditSum(extractIntValue(line));
                case "Total Amount Payed:" -> schufaOverview.setTotalAmountPayed(extractIntValue(line));
                case "Total Amount Owed:" -> schufaOverview.setTotalAmountOwed(extractIntValue(line));
                case "Total Monthly Rate:" -> schufaOverview.setTotalMonthlyRate(extractIntValue(line));
                case "Date Issued:" -> schufaOverview.setDateIssued(extractStringValue(line));
            }
        }

        return schufaOverview;
    }


    private int extractIntValue(String line) {
        return Integer.parseInt(line.split(":")[1].trim());
    }

    private double extractDoubleValue(String line) {
        return Double.parseDouble(line.split(":")[1].trim());
    }

    private String extractStringValue(String line) {
        return line.split(":")[1].trim();
    }


}