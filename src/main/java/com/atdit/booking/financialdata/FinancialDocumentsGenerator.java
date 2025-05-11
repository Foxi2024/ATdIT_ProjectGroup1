package com.atdit.booking.financialdata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FinancialDocumentsGenerator {

    private String directory;

    public FinancialDocumentsGenerator(String dir) {
        this.directory = dir;
    }

    public void generateDocumentFile(String content, String fileName) throws IOException {

        Path dirPath = Paths.get(this.directory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Path filePath = dirPath.resolve(fileName + ".txt");
        Files.writeString(filePath, content);
    }

    public void generateProofOfIncome(IncomeProof incomeProof, String filename) throws IOException {

        String content = "Monthly Net Income: " + incomeProof.monthlyNetIncome() + "\n" +
                         "Employer: " + incomeProof.employer() + "\n" +
                         "Employment Type: " + incomeProof.employmentType() + "\n" +
                         "Employment Duration: " + incomeProof.employmentDurationMonths() + "\n" +
                         "Date Issued: " + incomeProof.dateIssued();

        generateDocumentFile(content, filename);
    }

    public void generateProofOfLiquidAssets(LiquidAsset liquidAsset, String filename) throws IOException {

        String content = "Bank Account Balance: " + liquidAsset.balance() + "\n" +
                         "IBAN: " + liquidAsset.iban() + "\n" +
                         "Description: " + liquidAsset.description() + "\n" +
                         "Date Issued: " + liquidAsset.dateIssued();

        generateDocumentFile(content, filename);
    }

    public void generateSchufa(Schufaauskunft schufa, String filename) throws IOException {

        SchufaOverview schufaOverview = new SchufaOverview(schufa);

        String content = "First Name: " + schufaOverview.getFirstName() + "\n" +
                         "Last Name: " + schufaOverview.getLastName() + "\n" +
                         "Schufa Score: " + schufaOverview.getScore() + "\n" +
                         "Total Credits: " + schufaOverview.getTotalCredits() + "\n" +
                         "Total Credit Sum: " + schufaOverview.getTotalCreditSum() + "\n" +
                         "Total Amount Payed: "  + schufaOverview.getTotalAmountPayed() + "\n"+
                         "Total Amount Owed: " + schufaOverview.getTotalAmountOwed() + "\n" +
                         "Total Monthly Rate: " + schufaOverview.getTotalMonthlyRate() + "\n" +
                         "Date Issued: " + schufaOverview.getDateIssued();

        generateDocumentFile(content, filename);

    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

    public static void main(String[] args) {

        FinancialDocumentsGenerator generator = new FinancialDocumentsGenerator("financial_documents");

        Credit c1 = new Credit("Car Loan", 50_000, 5.0f, 500, 10000);
        Credit c2 = new Credit("Home Loan", 500_000, 3.5f, 1500, 250000);

        try {
            generator.generateProofOfIncome(new IncomeProof(32_000, "ABC Corp", "Full-time", 24, "2025-01-01"), "proof_of_income");
            generator.generateProofOfLiquidAssets(new LiquidAsset("DE12345678901234567890", "Savings Account", 1_000_000, "2025-01-01"), "proof_of_liquid_assets");
            generator.generateSchufa(new Schufaauskunft("Anton", "Beton", 0.75f, new ArrayList<Credit>(), "2023-01-01"), "schufa_report");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
