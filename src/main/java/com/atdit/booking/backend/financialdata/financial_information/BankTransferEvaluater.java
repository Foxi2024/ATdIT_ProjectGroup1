package com.atdit.booking.backend.financialdata.financial_information;

import com.atdit.booking.backend.exceptions.ValidationException;

public class BankTransferEvaluater {

    private BankTransferDetails bankTransferDetails;

    public void setBankTransferDetails(BankTransferDetails bankTransferDetails) {
        this.bankTransferDetails = bankTransferDetails;
    }

    private void validateBankTransferInfo() {
        StringBuilder errorMessage = new StringBuilder("Bitte korrigieren Sie folgende Fehler:\n");
        boolean isValid = true;

        String iban = this.bankTransferDetails.getIban();

        if (!iban.matches("^DE\\d{20}$")) {
            errorMessage.append("- Ungültige IBAN (Format: DE + 20 Ziffern)\n");
            isValid = false;
        }

        String bic = this.bankTransferDetails.getBicSwift();

        if (!bic.matches("^[A-Z0-9]{8}$") || !bic.matches("^[A-Z0-9]{11}$")) {
            errorMessage.append("- Ungültiger BIC/SWIFT Code (8 oder 11 Zeichen)\n");
            isValid = false;
        }


        String accountHolder = this.bankTransferDetails.getAccountHolder();

        if (accountHolder.isEmpty() || !accountHolder.matches("^[A-Za-zÄäÖöÜüß\\s-]{2,50}$")) {
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
}
