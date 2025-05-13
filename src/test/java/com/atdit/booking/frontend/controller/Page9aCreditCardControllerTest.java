package com.atdit.booking.frontend.controller;

import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.financial_information.CreditCardDetails;
import com.atdit.booking.backend.financialdata.processing.PaymentMethodEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Page9aCreditCardControllerTest {

    private PaymentMethodEvaluator evaluator;
    private CreditCardDetails creditCardDetails;

    @BeforeEach
    void setUp() {
        evaluator = new PaymentMethodEvaluator();
        creditCardDetails = new CreditCardDetails();
        evaluator.setCreditCardDetails(creditCardDetails);
    }

    @Test
    void validateCreditCardInfo_ValidData_NoException() {
        // Arrange
        creditCardDetails.setCardNumber("1234567890123456");
        creditCardDetails.setExpiryDate("12/25");
        creditCardDetails.setCvv("123");

        // Act & Assert
        assertDoesNotThrow(() -> evaluator.validateCreditCardInfo());
    }

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