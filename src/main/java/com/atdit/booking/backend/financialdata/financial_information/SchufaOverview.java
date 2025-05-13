package com.atdit.booking.backend.financialdata.financial_information;

/**
 * This class represents an overview of Schufa (German Credit Bureau) information.
 * It contains personal information, credit scores and various credit-related metrics.
 */
public class SchufaOverview {

    /** First name of the person */
    private String firstName;
    /** Last name of the person */
    private String lastName;
    /** Schufa score value */
    private double score;
    /** Total number of credits */
    private int totalCredits;
    /** Sum of all credits in cents */
    private int totalCreditSum;
    /** Total amount already paid for all credits in cents */
    private int totalAmountPayed;
    /** Total amount still owed for all credits in cents */
    private int totalAmountOwed;
    /** Sum of all monthly payments in cents */
    private int totalMonthlyRate;
    /** Date when the Schufa information was issued */
    private String dateIssued;

    /**
     * Constructor that creates a SchufaOverview from a Schufaauskunft object.
     * Calculates various totals from the credit list.
     *
     * @param schufa The Schufaauskunft object containing the source data
     */
    public SchufaOverview(Schufaauskunft schufa){

        this.firstName = schufa.firstName();
        this.lastName = schufa.lastName();
        this.score = schufa.score();
        this.totalCredits = schufa.creditList().size();
        this.totalCreditSum = schufa.creditList().stream().mapToInt(Credit::amount).sum();
        this.totalAmountPayed = schufa.creditList().stream().mapToInt(Credit::remainingSum).sum();
        this.totalAmountOwed = schufa.creditList().stream().mapToInt(Credit::amountOwed).sum();
        this.totalMonthlyRate = schufa.creditList().stream().mapToInt(Credit::monthlyPayment).sum();
        this.dateIssued = schufa.issueDate();

    }

    /**
     * Constructor that creates a SchufaOverview with specified credit metrics but empty name fields.
     *
     * @param score Credit score
     * @param totalCredits Number of credits
     * @param totalCreditSum Total sum of all credits
     * @param totalAmountPayed Total amount paid
     * @param totalAmountOwed Total amount still owed
     * @param totalMonthlyRate Total monthly payment rate
     * @param dateIssued Date of issuance
     */
    public SchufaOverview(double score, int totalCredits, int totalCreditSum, int totalAmountPayed, int totalAmountOwed, int totalMonthlyRate, String dateIssued) {
        this.firstName = "";
        this.lastName = "";
        this.score = score;
        this.totalCredits = totalCredits;
        this.totalCreditSum = totalCreditSum;
        this.totalAmountPayed = totalAmountPayed;
        this.totalAmountOwed = totalAmountOwed;
        this.totalMonthlyRate = totalMonthlyRate;
        this.dateIssued = dateIssued;
    }

    /**
     * Default constructor
     */
    public SchufaOverview() {}

    /**
     * @return The last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return The first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return The Schufa score
     */
    public double getScore() {
        return score;
    }

    /**
     * @return The total number of credits
     */
    public int getTotalCredits() {
        return totalCredits;
    }

    /**
     * @return The total sum of all credits
     */
    public int getTotalCreditSum() {
        return totalCreditSum;
    }

    /**
     * @return The total amount paid for all credits
     */
    public int getTotalAmountPayed() {
        return totalAmountPayed;
    }

    /**
     * @return The total amount still owed
     */
    public int getTotalAmountOwed() {
        return totalAmountOwed;
    }

    /**
     * @return The total monthly payment rate
     */
    public int getTotalMonthlyRate() {
        return totalMonthlyRate;
    }

    /**
     * @return The date when the information was issued
     */
    public String getDateIssued() {
        return dateIssued;
    }

    /**
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @param score The score to set
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * @param totalCredits The total number of credits to set
     */
    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    /**
     * @param totalCreditSum The total credit sum to set
     */
    public void setTotalCreditSum(int totalCreditSum) {
        this.totalCreditSum = totalCreditSum;
    }

    /**
     * @param totalAmountPayed The total amount paid to set
     */
    public void setTotalAmountPayed(int totalAmountPayed) {
        this.totalAmountPayed = totalAmountPayed;
    }

    /**
     * @param totalAmountOwed The total amount owed to set
     */
    public void setTotalAmountOwed(int totalAmountOwed) {
        this.totalAmountOwed = totalAmountOwed;
    }

    /**
     * @param totalMonthlyRate The total monthly rate to set
     */
    public void setTotalMonthlyRate(int totalMonthlyRate) {
        this.totalMonthlyRate = totalMonthlyRate;
    }

    /**
     * @param dateIssued The issue date to set
     */
    public void setDateIssued(String dateIssued) {
        this.dateIssued = dateIssued;
    }
}