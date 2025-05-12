package com.atdit.booking.backend.financialdata.financial_information;

public class SchufaOverview {

    private String firstName;
    private String lastName;
    private double score;
    private int totalCredits;
    private int totalCreditSum;
    private int totalAmountPayed;
    private int totalAmountOwed;
    private int totalMonthlyRate;
    private String dateIssued;

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

    public SchufaOverview() {}

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public double getScore() {
        return score;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public int getTotalCreditSum() {
        return totalCreditSum;
    }

    public int getTotalAmountPayed() {
        return totalAmountPayed;
    }

    public int getTotalAmountOwed() {
        return totalAmountOwed;
    }

    public int getTotalMonthlyRate() {
        return totalMonthlyRate;
    }

    public String getDateIssued() {
        return dateIssued;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public void setTotalCreditSum(int totalCreditSum) {
        this.totalCreditSum = totalCreditSum;
    }

    public void setTotalAmountPayed(int totalAmountPayed) {
        this.totalAmountPayed = totalAmountPayed;
    }

    public void setTotalAmountOwed(int totalAmountOwed) {
        this.totalAmountOwed = totalAmountOwed;
    }

    public void setTotalMonthlyRate(int totalMonthlyRate) {
        this.totalMonthlyRate = totalMonthlyRate;
    }

    public void setDateIssued(String dateIssued) {
        this.dateIssued = dateIssued;
    }
}
