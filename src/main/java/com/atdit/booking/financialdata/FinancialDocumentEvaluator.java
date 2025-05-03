package com.atdit.booking.financialdata;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class FinancialDocumentEvaluator {


    private final Map<String, String> parsedValues = new HashMap<>();
    private StringBuilder errorLog = new StringBuilder();


    public FinancialInformation evaluateDocuments(Map<String, String> documents) {
        FinancialInformation financialInfo = new FinancialInformation();

        documents.forEach((type, content) -> {
            if (evaluateDocument(type, content)) {
                updateFinancialInfo(financialInfo, type);
            }
        });

        return financialInfo;
    }

    public boolean evaluateDocument(String documentType, String content) {
        parsedValues.clear();
        errorLog = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new StringReader(content))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    processLine(line);
                }
            }
            return true;
        } catch (Exception e) {
            errorLog.append("Error reading document: ").append(e.getMessage());
            return false;
        }
    }

    private void processLine(String line) {
        if (line.contains(":")) {
            String[] parts = line.split(":", 2);
            String key = parts[0].trim();
            String value = parts.length > 1 ? parts[1].trim() : "";
            parsedValues.put(key, value);
        }
    }

    private void updateFinancialInfo(FinancialInformation info, String documentType) {
        switch (documentType) {
            case "income" -> processIncomeDocument(info);
            case "liquidAssets" -> processLiquidAssetsDocument(info);
            case "schufa" -> processSchufaDocument(info);
        }
    }

    private void processIncomeDocument(FinancialInformation info) {
        if (parsedValues.containsKey("Monthly Net Income")) {
            int income = Integer.parseInt(parsedValues.get("Monthly Net Income"));
            info.setAvgNetIncome(income);
        }
    }

    private void processLiquidAssetsDocument(FinancialInformation info) {
        if (parsedValues.containsKey("Bank Account Balance")) {
            int balance = Integer.parseInt(parsedValues.get("Bank Account Balance"));
            info.setLiquidAssets(balance);

            LiquidAsset asset = new LiquidAsset(
                    parsedValues.getOrDefault("IBAN", ""),
                    parsedValues.getOrDefault("Description", ""),
                    balance,
                    parsedValues.getOrDefault("Date Issued", "")
            );
            info.setLiquidAssets(balance);
        }
    }

    private void processSchufaDocument(FinancialInformation info) {
        if (parsedValues.containsKey("Schufa Score")) {
            float score = Float.parseFloat(parsedValues.get("Schufa Score"));
            ArrayList<Credit> credits = new ArrayList<>();
            String issueDate = parsedValues.getOrDefault("Issue Date", "");

            info.setSchufaauskunft(new Schufaauskunft(
                    score,
                    credits,
                    issueDate
            ));
        }
    }

    public String getErrorLog() {
        return errorLog.toString();
    }
}