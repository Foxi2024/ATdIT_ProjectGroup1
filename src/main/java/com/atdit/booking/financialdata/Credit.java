package com.atdit.booking.financialdata;

public record Credit(String description, int value, float interestRate, int monthlyPayment, int remainingSum) {

    public int amountOwed() {
        return value - remainingSum;
    }
}
