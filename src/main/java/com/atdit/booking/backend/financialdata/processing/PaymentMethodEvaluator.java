package com.atdit.booking.backend.financialdata.processing;

import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.financial_information.BankTransferDetails;
import com.atdit.booking.backend.financialdata.financial_information.CreditCardDetails;

/**
 * This class evaluates and validates payment method details for both bank transfers and credit cards.
 * It contains validation logic for bank account information and credit card data.
 */
public class PaymentMethodEvaluator {
    /** The bank transfer details to be validated */
    private BankTransferDetails bankTransferDetails;

    /** The credit card details to be validated */
    private CreditCardDetails creditCardDetails;

    /**
     * Sets the bank transfer details for validation.
     *
     * @param bankTransferDetails The bank transfer details to be set
     * @throws ValidationException If the provided details are invalid
     */
    public void setBankTransferDetails(BankTransferDetails bankTransferDetails) throws ValidationException{
        this.bankTransferDetails = bankTransferDetails;
    }

    /**
     * Sets the credit card details for validation.
     *
     * @param creditCardDetails The credit card details to be set
     */
    public void setCreditCardDetails(CreditCardDetails creditCardDetails) {
        this.creditCardDetails = creditCardDetails;
    }

    /**
     * Validates the bank transfer information according to German banking standards.
     * Checks the following criteria:
     * - IBAN must match German format (DE + 20 digits)
     * - BIC/SWIFT code must be 8 or 11 characters
     * - Account holder name must be 2-50 characters, letters only
     * - Bank name must be at least 2 characters
     *
     * Regex patterns used:
     * - IBAN: "^DE\\d{20}$" - Matches exactly DE followed by 20 digits
     * - BIC: "^[A-Z0-9]{8}$" or "^[A-Z0-9]{11}$" - Matches exactly 8 or 11 alphanumeric characters
     * - Account holder: "^[A-Za-zÄäÖöÜüß\\s-]{2,50}$" - Matches 2-50 characters of letters, German umlauts, spaces and hyphens
     * @throws ValidationException If any of the validation criteria are not met
     */
    public void validateBankTransferInfo() {
            StringBuilder errorMessage = new StringBuilder("Bitte korrigieren Sie folgende Fehler:\n");
            boolean isValid = true;

            String iban = this.bankTransferDetails.getIban();

            if (iban == null || !iban.matches("^DE\\d{20}$")) {
                errorMessage.append("- Ungültige IBAN (Format: DE + 20 Ziffern)\n");
                isValid = false;
            }

            String bic = this.bankTransferDetails.getBicSwift();

            if (bic == null || !bic.matches("^[A-Z0-9]{8}$") && !bic.matches("^[A-Z0-9]{11}$")) {
                errorMessage.append("- Ungültiger BIC/SWIFT Code (8 oder 11 Zeichen)\n");
                isValid = false;
            }


            String accountHolder = this.bankTransferDetails.getAccountHolder();

            if (accountHolder == null || accountHolder.isEmpty() || !accountHolder.matches("^[A-Za-zÄäÖöÜüß\\s-]{2,50}$")) {
                errorMessage.append("- Ungültiger Kontoinhaber (2-50 Zeichen, nur Buchstaben erlaubt)\n");
                isValid = false;
            }


            String bankName = this.bankTransferDetails.getBankName();

            if (bankName.length() < 2) {
                errorMessage.append("- Ungültiger Bankname (mindestens 2 Zeichen)\n");
                isValid = false;
            }

            if (!isValid) {
                throw new ValidationException(errorMessage.toString());
            }

        }
    /**
     * Validates credit card information according to standard credit card requirements.
     * Checks the following criteria:
     * - Card number must be exactly 16 digits
     * - Expiry date must be in MM/YY format and not expired
     * - CVV must be 3-4 digits
     *
     * Regex patterns used:
     * - Card number: "^\\d{16}$" - Matches exactly 16 digits
     * - Expiry date: "^(0[1-9]|1[0-2])/([0-9]{2})$" - Matches MM/YY format where MM is 01-12
     * - CVV: "^\\d{3,4}$" - Matches 3 or 4 digits
     * @throws ValidationException If any of the validation criteria are not met or if the card is expired
     */
    public void validateCreditCardInfo() {
        StringBuilder errorMessage = new StringBuilder("Bitte korrigieren Sie folgende Fehler:\n");
        boolean isValid = true;

        String cardNumber = this.creditCardDetails.getCardNumber();

        if (cardNumber == null || !cardNumber.matches("^\\d{16}$")) {
            errorMessage.append("- Ungültige Kartennummer (16 Ziffern erforderlich)\n");
            isValid = false;
        }

        String expiry = this.creditCardDetails.getExpiryDate();

        if (expiry == null || !expiry.matches("^(0[1-9]|1[0-2])/([0-9]{2})$")) {
            errorMessage.append("- Ungültiges Ablaufdatum (Format: MM/YY)\n");
            isValid = false;
        } else {
            try {
                String[] parts = expiry.split("/");
                int month = Integer.parseInt(parts[0]);
                int year = Integer.parseInt(parts[1]) + 2000;

                java.time.YearMonth cardDate = java.time.YearMonth.of(year, month);
                java.time.YearMonth now = java.time.YearMonth.now();

                if (cardDate.isBefore(now)) {
                    errorMessage.append("- Kreditkarte ist abgelaufen\n");
                    isValid = false;
                }
            } catch (Exception e) {
                errorMessage.append("- Ungültiges Ablaufdatum\n");
                isValid = false;
            }
        }

        String cvv = this.creditCardDetails.getCvv();

        if (cvv == null || !cvv.matches("^\\d{3,4}$")) {
            errorMessage.append("- Ungültiger CVV-Code (3-4 Ziffern erforderlich)\n");
            isValid = false;
        }

        if (!isValid) {
            throw new ValidationException(errorMessage.toString());
        }
    }


}
