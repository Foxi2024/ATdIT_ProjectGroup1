package com.atdit.booking.backend.financialdata.financial_information;

/**
 * Record representing an income proof document containing employment and income information.
 * This immutable record ensures data integrity through validation in its canonical constructor.
 *
 * @param monthlyNetIncome The monthly net income amount in currency units (must be non-negative)
 * @param employer The name of the employer (must not be null or empty)
 * @param employmentType The type of employment (e.g., full-time, part-time, must not be null or empty)
 * @param employmentDurationMonths The duration of employment in months (must be non-negative)
 * @param dateIssued The date when the income proof was issued
 * @throws IllegalArgumentException if any validation constraints are violated
 */
public record IncomeProof(
        int monthlyNetIncome,
        String employer,
        String employmentType,
        int employmentDurationMonths,
        String dateIssued
) {
    /**
     * Canonical constructor for IncomeProof that validates all input parameters.
     *
     * @throws IllegalArgumentException if monthlyNetIncome is negative
     * @throws IllegalArgumentException if employer is null or empty
     * @throws IllegalArgumentException if employmentType is null or empty
     * @throws IllegalArgumentException if employmentDurationMonths is negative
     */
    public IncomeProof {
        if (monthlyNetIncome < 0) {
            throw new IllegalArgumentException("Monthly net income cannot be negative");
        }
        if (employer == null || employer.trim().isEmpty()) {
            throw new IllegalArgumentException("Employer cannot be null or empty");
        }
        if (employmentType == null || employmentType.trim().isEmpty()) {
            throw new IllegalArgumentException("Employment type cannot be null or empty");
        }
        if (employmentDurationMonths < 0) {
            throw new IllegalArgumentException("Employment duration cannot be negative");
        }
    }
}