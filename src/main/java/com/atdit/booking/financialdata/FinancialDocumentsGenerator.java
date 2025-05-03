package com.atdit.booking.financialdata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FinancialDocumentsGenerator {

    public void generateDocumentFile(String content, String fileName, String directory) throws IOException {

        Path dirPath = Paths.get(directory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Path filePath = dirPath.resolve(fileName + ".txt");
        Files.writeString(filePath, content);
    }

    public void generateProofOfIncome(IncomeProof incomeProof) throws IOException {

        String content = "Monthly Net Income: " + incomeProof.monthlyNetIncome() + "\n" +
                         "Employer: " + incomeProof.employer() + "\n" +
                         "Employment Type: " + incomeProof.employmentType() + "\n" +
                         "Employment Duration: " + incomeProof.employmentDurationMonths() + " months";

        generateDocumentFile(content, "IncomeProof", "financial_documents");
    }

    public void generateProofOfLiquidAssets(LiquidAsset liquidAsset) throws IOException {

        String content = "Bank Account Balance: " + liquidAsset.balance() + "\n" +
                         "IBAN: " + liquidAsset.iban() + "\n" +
                         "Description: " + liquidAsset.description() + "\n" +
                         "Date Issued: " + liquidAsset.dateIssued();

        generateDocumentFile(content, "LiquidAssetsProof", "financial_documents");
    }

    public void generateSchufa(Schufaauskunft schufa) throws IOException {

        String content = "Schufa Score: " + schufa.score() + "\n" +
                         "Total Credits: " + schufa.creditList().size() + "\n" +
                         "Total Credit Sum: " + schufa.creditList().stream().mapToDouble(Credit::amount).sum() + "\n" +
                         "Total Amount Payed: "  + schufa.creditList().stream().mapToDouble(Credit::remainingSum).sum() + "\n"+
                         "Total Amount Owed: " + schufa.creditList().stream().mapToDouble(Credit::amountOwed).sum() + "\n" +
                         "Total Interest Rate: " + schufa.creditList().stream().mapToDouble(Credit::interestRate).sum() + "\n" +
                         "Issue Date: " + schufa.issueDate();

        generateDocumentFile(content, "Schufa", "financial_documents");

    }

    public static void main(String[] args) {
        FinancialDocumentsGenerator generator = new FinancialDocumentsGenerator();

        Credit c1 = new Credit("Car Loan", 20000, 5.0f, 500, 10000);
        Credit c2 = new Credit("Home Loan", 150000, 3.5f, 1500, 50000);

        try {
            generator.generateProofOfIncome(new IncomeProof(30000, "ABC Corp", "Full-time", 24));
            generator.generateProofOfLiquidAssets(new LiquidAsset("DE12345678901234567890", "Savings Account", 1000000, "2023-01-01"));
            generator.generateSchufa(new Schufaauskunft(0.75f, new ArrayList<Credit>(), "2023-01-01"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
