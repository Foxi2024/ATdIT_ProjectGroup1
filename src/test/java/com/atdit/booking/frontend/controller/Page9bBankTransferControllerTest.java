package com.atdit.booking.frontend.controller;

import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.financial_information.BankTransferDetails;
import com.atdit.booking.backend.financialdata.processing.PaymentMethodEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Page9bBankTransferControllerTest {

    private PaymentMethodEvaluator evaluator;
    private BankTransferDetails bankDetails;

    @BeforeEach
    void setUp() {
        evaluator = new PaymentMethodEvaluator();
        bankDetails = new BankTransferDetails();
        evaluator.setBankTransferDetails(bankDetails);
    }

    @Test
    void validateBankTransferInfo_ValidData_NoException() throws ValidationException {
        // Arrange
        bankDetails.setIban("DE12345678901234567890");
        bankDetails.setBicSwift("DEUTDE44");
        bankDetails.setAccountHolder("Max Mustermann");
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        assertDoesNotThrow(() -> evaluator.validateBankTransferInfo());
    }

    @Test
    void validateBankTransferInfo_InvalidIban_ThrowsException() {
        // Arrange
        bankDetails.setIban("DE123"); // zu kurz
        bankDetails.setBicSwift("DEUTDEDB123");
        bankDetails.setAccountHolder("Max Mustermann");
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültige IBAN"));
    }

    @Test
    void validateBankTransferInfo_NullIban_ThrowsException() {
        // Arrange
        // Setze andere Werte, aber lasse IBAN null
        bankDetails.setBicSwift("DEUTDE44");
        bankDetails.setAccountHolder("Max Mustermann");
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültige IBAN"));
    }

    @Test
    void validateBankTransferInfo_InvalidBic_ThrowsException() {
        // Arrange
        bankDetails.setIban("DE12345678901234567890");
        bankDetails.setBicSwift("ABC"); // zu kurz
        bankDetails.setAccountHolder("Max Mustermann");
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültiger BIC/SWIFT Code"));
    }

    @Test
    void validateBankTransferInfo_NullBic_ThrowsException() {
        // Arrange
        bankDetails.setIban("DE12345678901234567890");
        // Lasse BIC null
        bankDetails.setAccountHolder("Max Mustermann");
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültiger BIC/SWIFT Code"));
    }

    @Test
    void validateBankTransferInfo_InvalidAccountHolder_ThrowsException() {
        // Arrange
        bankDetails.setIban("DE12345678901234567890");
        bankDetails.setBicSwift("DEUTDEDB123");
        bankDetails.setAccountHolder("M"); // zu kurz
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültiger Kontoinhaber"));
    }

    @Test
    void validateBankTransferInfo_AccountHolderWithSpecialChars_ThrowsException() {
        // Arrange
        bankDetails.setIban("DE12345678901234567890");
        bankDetails.setBicSwift("DEUTDEDB123");
        bankDetails.setAccountHolder("Max123!@#"); // ungültige Zeichen
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültiger Kontoinhaber"));
    }

    @Test
    void validateBankTransferInfo_InvalidBankName_ThrowsException() {
        // Arrange
        bankDetails.setIban("DE12345678901234567890");
        bankDetails.setBicSwift("DEUTDEDB123");
        bankDetails.setAccountHolder("Max Mustermann");
        bankDetails.setBankName("D"); // zu kurz

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültiger Bankname"));
    }

    @Test
    void validateBankTransferInfo_MultipleErrors_ThrowsExceptionWithAllErrors() {
        // Arrange
        bankDetails.setIban("DE123"); // ungültig
        bankDetails.setBicSwift("ABC"); // ungültig
        bankDetails.setAccountHolder("M"); // ungültig
        bankDetails.setBankName("D"); // ungültig

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        String errorMessage = exception.getMessage();
        assertTrue(errorMessage.contains("Ungültige IBAN"));
        assertTrue(errorMessage.contains("Ungültiger BIC/SWIFT Code"));
        assertTrue(errorMessage.contains("Ungültiger Kontoinhaber"));
        assertTrue(errorMessage.contains("Ungültiger Bankname"));
    }

    @Test
    void validateBankTransferInfo_AccountHolderWithUmlauts_NoException() {
        // Arrange
        bankDetails.setIban("DE12345678901234567890");
        bankDetails.setBicSwift("DEUTDEBB"); // Gültiger 8-stelliger BIC
        bankDetails.setAccountHolder("Jörg Müller");
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        assertDoesNotThrow(() -> evaluator.validateBankTransferInfo());
    }
}