/**
 * A class for generating various financial documents in text format.
 * This class handles the creation of documents such as proof of income, liquid assets proof,
 * and Schufa (German credit score) reports.
 */
package com.atdit.booking.backend.financialdata.processing;

import com.atdit.booking.backend.financialdata.financial_information.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * The FinancialDocumentsGenerator class provides functionality to generate and save
 * different types of financial documents as text files.
 */
public class FinancialDocumentsGenerator {

    /** The directory path where generated documents will be stored */
    private String directory;

    /**
     * Constructs a new FinancialDocumentsGenerator with the specified output directory.
     *
     * @param dir The directory path where documents should be saved
     */
    public FinancialDocumentsGenerator(String dir) {
        this.directory = dir;
    }

    /**
     * Generates a text file with the provided content in the specified directory.
     * Creates the directory if it doesn't exist.
     *
     * @param content The text content to write to the file
     * @param fileName The name of the file (without extension)
     * @throws IOException If an I/O error occurs while writing the file
     */
    public void generateDocumentFile(String content, String fileName) throws IOException {

        Path dirPath = Paths.get(this.directory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Path filePath = dirPath.resolve(fileName + ".txt");
        Files.writeString(filePath, content);
    }

    /**
     * Generates a proof of income document from the provided income information.
     *
     * @param incomeProof The income proof information
     * @param filename The name of the output file (without extension)
     * @throws IOException If an I/O error occurs while writing the file
     */
    public void generateProofOfIncome(IncomeProof incomeProof, String filename) throws IOException {

        String content = "Monthly Net Income: " + incomeProof.monthlyNetIncome() + "\n" +
                         "Employer: " + incomeProof.employer() + "\n" +
                         "Employment Type: " + incomeProof.employmentType() + "\n" +
                         "Employment Duration: " + incomeProof.employmentDurationMonths() + "\n" +
                         "Date Issued: " + incomeProof.dateIssued();

        generateDocumentFile(content, filename);
    }

    /**
     * Generates a liquid assets proof document from the provided asset information.
     *
     * @param liquidAsset The liquid asset information
     * @param filename The name of the output file (without extension)
     * @throws IOException If an I/O error occurs while writing the file
     */
    public void generateProofOfLiquidAssets(LiquidAsset liquidAsset, String filename) throws IOException {

        String content = "Bank Account Balance: " + liquidAsset.balance() + "\n" +
                         "IBAN: " + liquidAsset.iban() + "\n" +
                         "Description: " + liquidAsset.description() + "\n" +
                         "Date Issued: " + liquidAsset.dateIssued();

        generateDocumentFile(content, filename);
    }

    /**
     * Generates a Schufa report document from the provided Schufa information.
     *
     * @param schufa The Schufa information
     * @param filename The name of the output file (without extension)
     * @throws IOException If an I/O error occurs while writing the file
     */
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

    /**
     * Sets a new output directory for the document generator.
     *
     * @param directory The new directory path
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Gets the current output directory of the document generator.
     *
     * @return The current directory path
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Main method demonstrating the usage of the FinancialDocumentsGenerator.
     * Creates example documents for income proof, liquid assets, and Schufa report.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {

        FinancialDocumentsGenerator generator = new FinancialDocumentsGenerator("financial_proofs");

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