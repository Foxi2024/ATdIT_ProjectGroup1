package com.atdit.booking.financialdata;

public record Credit(String description, int amount, float interestRate, int monthlyPayment, int remainingSum) {

    public int amountOwed() {
        return amount - remainingSum;
    }
}
