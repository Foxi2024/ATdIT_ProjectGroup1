package com.atdit.booking.financialdata;

public record IncomeProof(
        int monthlyNetIncome,
        String employer,
        String employmentType,
        int employmentDurationMonths
) {
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