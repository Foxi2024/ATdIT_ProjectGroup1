package com.atdit.booking.backend.financialdata.financial_information;

/**
 * Represents a credit/loan record with its essential financial information.
 * This immutable record holds details about a credit, including description,
 * amount, interest rate, monthly payment and the remaining sum to be paid.
 *
 * @param description    The description or purpose of the credit
 * @param amount        The total amount of the credit in currency units
 * @param interestRate  The interest rate of the credit as a decimal number
 * @param monthlyPayment The fixed monthly payment amount in currency units
 * @param remainingSum  The remaining sum to be paid in currency units
 */
public record Credit(String description, int amount, double interestRate, int monthlyPayment, int remainingSum) {

    /**
     * Calculates the amount that has already been paid back for this credit
     *
     * @return The difference between the total amount and the remaining sum in currency units
     */
    public int amountOwed() {
        return amount - remainingSum;
    }
}