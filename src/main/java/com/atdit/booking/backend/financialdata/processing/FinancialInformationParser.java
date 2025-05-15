package com.atdit.booking.backend.financialdata.processing;

import com.atdit.booking.backend.financialdata.financial_information.IncomeProof;
import com.atdit.booking.backend.financialdata.financial_information.LiquidAsset;
import com.atdit.booking.backend.financialdata.financial_information.SchufaOverview;

/**
 * A parser class for processing various types of financial information documents.
 * This class provides methods to parse income proofs, liquid assets, and Schufa documents
 * from string content into structured objects.
 */
public class FinancialInformationParser {

    /**
     * Parses a document containing income information into an IncomeProof object.
     *
     * @param content The string content of the income document to be parsed
     * @return An IncomeProof object containing the parsed information
     * @throws IllegalArgumentException if the document format is invalid or contains incorrect data
     */
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

    /**
     * Parses a document containing liquid assets information into a LiquidAsset object.
     *
     * @param content The string content of the liquid assets document to be parsed
     * @return A LiquidAsset object containing the parsed information
     * @throws IllegalArgumentException if the document format is invalid or contains incorrect data
     */
    public LiquidAsset parseLiquidAssetsDocument(String content) throws IllegalArgumentException {

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

    /**
     * Parses a Schufa document into a SchufaOverview object.
     *
     * @param content The string content of the Schufa document to be parsed
     * @return A SchufaOverview object containing the parsed information
     */
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

    /**
     * Extracts an integer value from a line of text.
     *
     * @param line The line of text containing the integer value after a colon
     * @return The extracted integer value
     * @throws IllegalArgumentException if the number format is invalid
     */
    private int extractIntValue(String line) {

        try {
            String value = line.split(":")[1].trim();
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in line: " + line);
        }
    }

    /**
     * Extracts a double value from a line of text.
     *
     * @param line The line of text containing the double value after a colon
     * @return The extracted double value
     * @throws IllegalArgumentException if the decimal format is invalid
     */
    private double extractDoubleValue(String line) {

        try {
            String value = line.split(":")[1].trim();
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid decimal format in line: " + line);
        }
    }

    /**
     * Extracts a string value from a line of text.
     *
     * @param line The line of text containing the string value after a colon
     * @return The extracted string value
     */
    private String extractStringValue(String line) {
        return line.split(":")[1].trim();
    }

}