package com.atdit.booking.frontend.controllers.payment_process;

import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.financial_information.CreditCardDetails;
import com.atdit.booking.backend.financialdata.processing.PaymentMethodEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for validating credit card information processing.
 * This class tests various scenarios of credit card validation using the PaymentMethodEvaluator.
 */
class Page3aCreditCardControllerTest {

    private PaymentMethodEvaluator evaluator;
    private CreditCardDetails creditCardDetails;

    /**
     * Sets up the test environment before each test.
     * Initializes a new PaymentMethodEvaluator and CreditCardDetails instance.
     */
    @BeforeEach
    void setUp() {
        evaluator = new PaymentMethodEvaluator();
        creditCardDetails = new CreditCardDetails();
        evaluator.setCreditCardDetails(creditCardDetails);
    }

    /**
     * Tests validation of valid credit card information.
     * Expects no exception to be thrown when all data is valid.
     */
    @Test
    void validateCreditCardInfo_ValidData_NoException() {
        // Arrange
        creditCardDetails.setCardNumber("1234567890123456");
        creditCardDetails.setExpiryDate("12/25");
        creditCardDetails.setCvv("123");

        // Act & Assert
        assertDoesNotThrow(() -> evaluator.validateCreditCardInfo());
    }

    /**
     * Tests validation with an invalid card number.
     * Expects a ValidationException when the card number is too short.
     */
    @Test
    void validateCreditCardInfo_InvalidCardNumber_ThrowsException() {
        // Arrange
        creditCardDetails.setCardNumber("123"); // zu kurz
        creditCardDetails.setExpiryDate("12/25");
        creditCardDetails.setCvv("123");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateCreditCardInfo());
        assertTrue(exception.getMessage().contains("Ungültige Kartennummer"));
    }

    /**
     * Tests validation with a null card number.
     * Expects a ValidationException when the card number is null.
     */
    @Test
    void validateCreditCardInfo_NullCardNumber_ThrowsException() {
        // Arrange
        creditCardDetails.setCardNumber(null);
        creditCardDetails.setExpiryDate("12/25");
        creditCardDetails.setCvv("123");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateCreditCardInfo());
        assertTrue(exception.getMessage().contains("Ungültige Kartennummer"));
    }

    /**
     * Tests validation with an invalid expiry date format.
     * Expects a ValidationException when the month in expiry date is invalid.
     */
    @Test
    void validateCreditCardInfo_InvalidExpiryFormat_ThrowsException() {
        // Arrange
        creditCardDetails.setCardNumber("1234567890123456");
        creditCardDetails.setExpiryDate("13/25"); // ungültiger Monat
        creditCardDetails.setCvv("123");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateCreditCardInfo());
        assertTrue(exception.getMessage().contains("Ungültiges Ablaufdatum"));
    }

    /**
     * Tests validation with an expired card.
     * Expects a ValidationException when the card has already expired.
     */
    @Test
    void validateCreditCardInfo_ExpiredCard_ThrowsException() {
        // Arrange
        creditCardDetails.setCardNumber("1234567890123456");
        creditCardDetails.setExpiryDate("01/20"); // abgelaufen
        creditCardDetails.setCvv("123");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateCreditCardInfo());
        assertTrue(exception.getMessage().contains("Kreditkarte ist abgelaufen"));
    }

    /**
     * Tests validation with an invalid CVV.
     * Expects a ValidationException when the CVV is too short.
     */
    @Test
    void validateCreditCardInfo_InvalidCvv_ThrowsException() {
        // Arrange
        creditCardDetails.setCardNumber("1234567890123456");
        creditCardDetails.setExpiryDate("12/25");
        creditCardDetails.setCvv("12"); // zu kurz

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateCreditCardInfo());
        assertTrue(exception.getMessage().contains("Ungültiger CVV-Code"));
    }

    /**
     * Tests validation with a null CVV.
     * Expects a ValidationException when the CVV is null.
     */
    @Test
    void validateCreditCardInfo_NullCvv_ThrowsException() {
        // Arrange
        creditCardDetails.setCardNumber("1234567890123456");
        creditCardDetails.setExpiryDate("12/25");
        creditCardDetails.setCvv(null);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateCreditCardInfo());
        assertTrue(exception.getMessage().contains("Ungültiger CVV-Code"));
    }

    /**
     * Tests validation with multiple invalid fields.
     * Expects a ValidationException containing multiple error messages when multiple fields are invalid.
     */
    @Test
    void validateCreditCardInfo_MultipleErrors_ThrowsExceptionWithAllErrors() {
        // Arrange
        creditCardDetails.setCardNumber("123"); // ungültig
        creditCardDetails.setExpiryDate("13/25"); // ungültig
        creditCardDetails.setCvv("12"); // ungültig

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> evaluator.validateCreditCardInfo());
        String errorMessage = exception.getMessage();
        assertTrue(errorMessage.contains("Ungültige Kartennummer"));
        assertTrue(errorMessage.contains("Ungültiges Ablaufdatum"));
        assertTrue(errorMessage.contains("Ungültiger CVV-Code"));
    }
}