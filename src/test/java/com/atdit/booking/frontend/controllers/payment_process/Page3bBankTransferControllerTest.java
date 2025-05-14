package com.atdit.booking.frontend.controllers.payment_process;

/**
 * Test class for validating bank transfer information through PaymentMethodEvaluator.
 * Tests various scenarios for bank transfer details validation including valid and invalid cases.
 */

import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.financial_information.BankTransferDetails;
import com.atdit.booking.backend.financialdata.processing.PaymentMethodEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Page3bBankTransferControllerTest {

    private PaymentMethodEvaluator evaluator;
    private BankTransferDetails bankDetails;

    /**
     * Sets up the test environment before each test.
     * Initializes a new PaymentMethodEvaluator and BankTransferDetails instance.
     */
    @BeforeEach
    void setUp() {
        evaluator = new PaymentMethodEvaluator();
        bankDetails = new BankTransferDetails();
        evaluator.setBankTransferDetails(bankDetails);
    }

    /**
     * Tests validation with valid bank transfer information.
     * Expects no exception to be thrown.
     */
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

    /**
     * Tests validation with an invalid IBAN.
     * Expects a ValidationException with appropriate error message.
     */
    @Test
    void validateBankTransferInfo_InvalidIban_ThrowsException() {
        // Arrange
        bankDetails.setIban("DE123"); // too short
        bankDetails.setBicSwift("DEUTDEDB123");
        bankDetails.setAccountHolder("Max Mustermann");
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültige IBAN"));
    }

    /**
     * Tests validation with null IBAN.
     * Expects a ValidationException with appropriate error message.
     */
    @Test
    void validateBankTransferInfo_NullIban_ThrowsException() {
        // Arrange
        // Set other values but leave IBAN null
        bankDetails.setBicSwift("DEUTDE44");
        bankDetails.setAccountHolder("Max Mustermann");
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültige IBAN"));
    }

    /**
     * Tests validation with invalid BIC/SWIFT code.
     * Expects a ValidationException with appropriate error message.
     */
    @Test
    void validateBankTransferInfo_InvalidBic_ThrowsException() {
        // Arrange
        bankDetails.setIban("DE12345678901234567890");
        bankDetails.setBicSwift("ABC"); // too short
        bankDetails.setAccountHolder("Max Mustermann");
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültiger BIC/SWIFT Code"));
    }

    /**
     * Tests validation with null BIC/SWIFT code.
     * Expects a ValidationException with appropriate error message.
     */
    @Test
    void validateBankTransferInfo_NullBic_ThrowsException() {
        // Arrange
        bankDetails.setIban("DE12345678901234567890");
        // Leave BIC null
        bankDetails.setAccountHolder("Max Mustermann");
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültiger BIC/SWIFT Code"));
    }

    /**
     * Tests validation with invalid account holder name (too short).
     * Expects a ValidationException with appropriate error message.
     */
    @Test
    void validateBankTransferInfo_InvalidAccountHolder_ThrowsException() {
        // Arrange
        bankDetails.setIban("DE12345678901234567890");
        bankDetails.setBicSwift("DEUTDEDB123");
        bankDetails.setAccountHolder("M"); // too short
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültiger Kontoinhaber"));
    }

    /**
     * Tests validation with account holder name containing special characters.
     * Expects a ValidationException with appropriate error message.
     */
    @Test
    void validateBankTransferInfo_AccountHolderWithSpecialChars_ThrowsException() {
        // Arrange
        bankDetails.setIban("DE12345678901234567890");
        bankDetails.setBicSwift("DEUTDEDB123");
        bankDetails.setAccountHolder("Max123!@#"); // invalid characters
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültiger Kontoinhaber"));
    }

    /**
     * Tests validation with invalid bank name (too short).
     * Expects a ValidationException with appropriate error message.
     */
    @Test
    void validateBankTransferInfo_InvalidBankName_ThrowsException() {
        // Arrange
        bankDetails.setIban("DE12345678901234567890");
        bankDetails.setBicSwift("DEUTDEDB123");
        bankDetails.setAccountHolder("Max Mustermann");
        bankDetails.setBankName("D"); // too short

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        assertTrue(exception.getMessage().contains("Ungültiger Bankname"));
    }

    /**
     * Tests validation with multiple invalid fields.
     * Expects a ValidationException containing all error messages.
     */
    @Test
    void validateBankTransferInfo_MultipleErrors_ThrowsExceptionWithAllErrors() {
        // Arrange
        bankDetails.setIban("DE123"); // invalid
        bankDetails.setBicSwift("ABC"); // invalid
        bankDetails.setAccountHolder("M"); // invalid
        bankDetails.setBankName("D"); // invalid

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateBankTransferInfo());
        String errorMessage = exception.getMessage();
        assertTrue(errorMessage.contains("Ungültige IBAN"));
        assertTrue(errorMessage.contains("Ungültiger BIC/SWIFT Code"));
        assertTrue(errorMessage.contains("Ungültiger Kontoinhaber"));
        assertTrue(errorMessage.contains("Ungültiger Bankname"));
    }

    /**
     * Tests validation with account holder name containing umlauts.
     * Expects no exception as umlauts should be valid characters.
     */
    @Test
    void validateBankTransferInfo_AccountHolderWithUmlauts_NoException() {
        // Arrange
        bankDetails.setIban("DE12345678901234567890");
        bankDetails.setBicSwift("DEUTDEBB"); // Valid 8-digit BIC
        bankDetails.setAccountHolder("Jörg Müller");
        bankDetails.setBankName("Deutsche Bank");

        // Act & Assert
        assertDoesNotThrow(() -> evaluator.validateBankTransferInfo());
    }
}