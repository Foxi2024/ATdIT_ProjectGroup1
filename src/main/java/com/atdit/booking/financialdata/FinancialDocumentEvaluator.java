package com.atdit.booking.financialdata;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class FinancialDocumentEvaluator {

    private final Map<String, String> parsedValues = new HashMap<>();
    private StringBuilder errorLog = new StringBuilder();

    public boolean evaluateDocument(String documentType, String content) {
        parsedValues.clear();
        errorLog = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new StringReader(content))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    if (!processLine(line)) {
                        return false;
                    }
                }
            }

            return validateDocument(documentType);
        } catch (Exception e) {
            errorLog.append("Error reading document: ").append(e.getMessage());
            return false;
        }
    }

    private boolean processLine(String line) {
        if (!line.contains(":")) {
            errorLog.append("Invalid line format: ").append(line).append("\n");
            return false;
        }

        String[] parts = line.split(":", 2);
        if (parts.length != 2) {
            errorLog.append("Invalid line format: ").append(line).append("\n");
            return false;
        }

        String attribute = parts[0].trim();
        String value = parts[1].trim();

        if (value.isEmpty()) {
            errorLog.append("Empty amount for attribute: ").append(attribute).append("\n");
            return false;
        }

        parsedValues.put(attribute, value);
        return true;
    }

    private boolean validateDocument(String documentType) {
        switch (documentType) {
            case "income":
                return validateIncomeDocument();
            case "liquidAssets":
                return validateLiquidAssetsDocument();
            case "fixedAssets":
                return validateFixedAssetsDocument();
            case "schufa":
                return validateSchufaDocument();
            default:
                errorLog.append("Unknown document type: ").append(documentType);
                return false;
        }
    }

    private boolean validateIncomeDocument() {
        String[] requiredFields = {
                "Monthly Net Income",
                "Employer",
                "Employment Duration",
                "Employment Type"
        };
        return checkRequiredFields(requiredFields);
    }

    private boolean validateLiquidAssetsDocument() {
        String[] requiredFields = {
                "Bank Balance",
                "Savings Account Balance",
                "Investment Portfolio Value",
                "Bank Name"
        };
        return checkRequiredFields(requiredFields);
    }

    private boolean validateFixedAssetsDocument() {
        String[] requiredFields = {
                "Real Estate Value",
                "Vehicle Value",
                "Other Assets Value",
                "Total Fixed Assets"
        };
        return checkRequiredFields(requiredFields);
    }

    private boolean validateSchufaDocument() {
        String[] requiredFields = {
                "Schufa Score",
                "Credit Rating",
                "Payment History",
                "Issue Date"
        };
        return checkRequiredFields(requiredFields);
    }

    private boolean checkRequiredFields(String[] requiredFields) {
        for (String field : requiredFields) {
            if (!parsedValues.containsKey(field)) {
                errorLog.append("Missing required field: ").append(field).append("\n");
                return false;
            }
        }
        return true;
    }

    public Map<String, String> getParsedValues() {
        return new HashMap<>(parsedValues);
    }

    public String getErrorLog() {
        return errorLog.toString();
    }
}