package com.atdit.booking.backend.financialdata.financial_information;

/**
 * A record representing a liquid asset in a financial context.
 * Liquid assets are assets that can be easily converted into cash without significant loss of value.
 *
 * @param iban        The International Bank Account Number (IBAN) associated with the liquid asset
 * @param description A descriptive text providing details about the liquid asset
 * @param balance     The current balance of the liquid asset in the smallest currency unit (e.g., cents)
 * @param dateIssued  The date when the liquid asset information was issued/recorded
 */
public record LiquidAsset(String iban, String description, int balance, String dateIssued) {

}