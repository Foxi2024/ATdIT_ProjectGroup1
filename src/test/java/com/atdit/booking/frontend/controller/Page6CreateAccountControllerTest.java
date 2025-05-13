package com.atdit.booking.frontend.controller;

import com.atdit.booking.backend.database.DatabaseService;
import com.atdit.booking.backend.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Page6CreateAccountControllerTest {
    private DatabaseService databaseService;

    @BeforeEach
    void setUp() throws Exception {
        databaseService = new DatabaseService();
    }

    @Test
    void testValidPassword() {
        assertDoesNotThrow(() ->
                databaseService.validatePasswords("Test1234!", "Test1234!")
        );
    }

    @Test
    void testPasswordsDoNotMatch() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("Test1234!", "Test1234?")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    @Test
    void testPasswordTooShort() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("Test1!", "Test1!")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    @Test
    void testPasswordWithoutUpperCase() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("test1234!", "test1234!")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    @Test
    void testPasswordWithoutLowerCase() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("TEST1234!", "TEST1234!")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    @Test
    void testPasswordWithoutDigit() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("TestTest!", "TestTest!")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    @Test
    void testPasswordWithoutSpecialChar() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("TestTest1", "TestTest1")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    @Test
    void testEmptyPasswords() {
        ValidationException exception = assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("", "")
        );
        assertEquals("Passwort erfüllt nicht die Anforderungen", exception.getMessage());
    }

    @Test
    void testNullPasswords() throws Exception {
        // Test für null als erstes Passwort
        assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords(null, "Test1234!")
        );

        // Test für null als zweites Passwort
        assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords("Test1234!", null)
        );

        // Test für beide Passwörter null
        assertThrows(ValidationException.class, () ->
                databaseService.validatePasswords(null, null)
        );
    }

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
