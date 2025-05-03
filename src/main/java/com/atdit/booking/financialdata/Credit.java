package com.atdit.booking.financialdata;

public record Credit(String description, int amount, double interestRate, int monthlyPayment, int remainingSum) {

    public int amountOwed() {
        return amount - remainingSum;
    }
}
