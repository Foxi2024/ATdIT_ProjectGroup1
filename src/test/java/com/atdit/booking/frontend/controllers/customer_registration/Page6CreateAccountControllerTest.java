package com.atdit.booking.frontend.controllers.customer_registration;

import com.atdit.booking.backend.database.DatabaseService;
import com.atdit.booking.backend.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Test class for password validation functionality in Page6CreateAccountController.
 * Tests various scenarios of password validation including matching passwords,
 * password complexity requirements, and edge cases.
 */
public class Page6CreateAccountControllerTest {
    private DatabaseService databaseService;

    /**
     * Sets up the test environment before each test.
     * Initializes a new instance of DatabaseService.
     *
     * @throws Exception if initialization fails
     */
    @BeforeEach
    void setUp() throws Exception {
        databaseService = new DatabaseService();
    }

    /**
     * Tests that a valid password meeting all requirements is accepted.
     */
    @Test
    void testValidPassword() {
        assertDoesNotThrow(() ->
                databaseService.validatePasswords("Test1234!", "Test1234!")
        );
    }

    /**
     * Tests that an exception is thrown when passwords don't match.
     */
    @Test
    void testPasswordsDoNotMatch() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("Test1234!", "Test1234?")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    /**
     * Tests that an exception is thrown when password is too short.
     */
    @Test
    void testPasswordTooShort() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("Test1!", "Test1!")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    /**
     * Tests that an exception is thrown when password doesn't contain uppercase letters.
     */
    @Test
    void testPasswordWithoutUpperCase() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("test1234!", "test1234!")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    /**
     * Tests that an exception is thrown when password doesn't contain lowercase letters.
     */
    @Test
    void testPasswordWithoutLowerCase() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("TEST1234!", "TEST1234!")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    /**
     * Tests that an exception is thrown when password doesn't contain digits.
     */
    @Test
    void testPasswordWithoutDigit() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("TestTest!", "TestTest!")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    /**
     * Tests that an exception is thrown when password doesn't contain special characters.
     */
    @Test
    void testPasswordWithoutSpecialChar() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("TestTest1", "TestTest1")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    /**
     * Tests that an exception is thrown when passwords are empty strings.
     */
    @Test
    void testEmptyPasswords() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("", "")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    /**
     * Tests various scenarios with null passwords.
     * Verifies that exceptions are thrown for null values in either or both password fields.
     *
     * @throws Exception if an unexpected error occurs
     */
    @Test
    void testNullPasswords() throws Exception {
        assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords(null, "Test1234!")
        );

        assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("Test1234!", null)
        );

        assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords(null, null)
        );
    }

    /**
     * Tests multiple valid password combinations with varying complexity.
     * All test cases should pass without throwing exceptions.
     */
    @Test
    void testComplexValidPasswords() {
        String[] validPasswords = {
                "Test1234!@",
                "Complex1!Password",
                "MyP@ssw0rd123",
                "Abcd1234!@#$"
        };

        for (String password : validPasswords) {
            assertDoesNotThrow(() ->
                    databaseService.validatePasswords(password, password)
            );
        }
    }
}