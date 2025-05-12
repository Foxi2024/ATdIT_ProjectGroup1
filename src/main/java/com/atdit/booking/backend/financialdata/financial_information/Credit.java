package com.atdit.booking.backend.financialdata.financial_information;

public record Credit(String description, int amount, double interestRate, int monthlyPayment, int remainingSum) {

    public int amountOwed() {
        return amount - remainingSum;
    }
}
