package com.atdit.booking.backend.financialdata.financial_information;

import com.atdit.booking.backend.exceptions.ValidationException;

public class PaymentMethodEvaluator {

    private BankTransferDetails bankTransferDetails;
    private CreditCardDetails creditCardDetails;

    public void setBankTransferDetails(BankTransferDetails bankTransferDetails) throws ValidationException{
        this.bankTransferDetails = bankTransferDetails;
    }

    public void setCreditCardDetails(CreditCardDetails creditCardDetails) {
        this.creditCardDetails = creditCardDetails;
    }

    public void validateBankTransferInfo() {
            StringBuilder errorMessage = new StringBuilder("Bitte korrigieren Sie folgende Fehler:\n");
            boolean isValid = true;

            String iban = this.bankTransferDetails.getIban();

            if (iban == null || !iban.matches("^DE\\d{20}$")) {
                errorMessage.append("- Ungültige IBAN (Format: DE + 20 Ziffern)\n");
                isValid = false;
            }

            String bic = this.bankTransferDetails.getBicSwift();

            if (bic == null || !bic.matches("^[A-Z0-9]{8}$") || !bic.matches("^[A-Z0-9]{11}$")) {
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
