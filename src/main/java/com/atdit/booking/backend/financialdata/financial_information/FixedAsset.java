package com.atdit.booking.backend.financialdata.financial_information;

/**
 * Represents a fixed asset in the financial system.
 * This record class encapsulates the essential information about a fixed asset,
 * including its monetary value, description, and confirmation status.
 *
 * @param value The monetary value of the fixed asset in the smallest currency unit (e.g., cents)
 * @param description A textual description of the fixed asset
 * @param confirmed Indicates whether the fixed asset has been confirmed/verified
 */
public record FixedAsset(int value, String description, boolean confirmed) {

}