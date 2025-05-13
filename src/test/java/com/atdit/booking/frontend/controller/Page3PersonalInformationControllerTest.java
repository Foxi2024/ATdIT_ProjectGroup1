package com.atdit.booking.frontend.controller;

import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.customer.CustomerEvaluator;
import com.atdit.booking.backend.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for validating customer personal information in the booking system.
 * This class tests various validation scenarios for customer data including:
 * - First name validation
 * - Last name validation
 * - Birthdate validation
 * - Country validation
 * - Address validation
 * - Email validation
 */
public class Page3PersonalInformationControllerTest {
    private CustomerEvaluator evaluator;
    private Customer validCustomer;

    /**
     * Sets up the test environment before each test.
     * Creates a valid customer object with sample data and initializes the customer evaluator.
     */
    @BeforeEach
    void setUp() {
        validCustomer = new Customer();
        validCustomer.setFirstName("Max");
        validCustomer.setName("Mustermann");
        validCustomer.setBirthdate("1990-01-01");
        validCustomer.setCountry("Deutschland");
        validCustomer.setAddress("Musterstr. 1");
        validCustomer.setEmail("max@example.com");
        evaluator = new CustomerEvaluator(validCustomer);
    }

    /**
     * Tests validation of first name when it is null.
     */
    @Test
    void checkFirstName_ShouldThrowException_WhenFirstNameIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkFirstName(null);
        });
        assert(exception.getMessage().contains("Kein Vorname angegeben"));
    }

    /**
     * Tests validation of first name when it is empty.
     */
    @Test
    void checkFirstName_ShouldThrowException_WhenFirstNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkFirstName("");
        });
        assert(exception.getMessage().contains("Kein Vorname angegeben"));
    }

    /**
     * Tests validation of first name when it contains numbers.
     */
    @Test
    void checkFirstName_ShouldThrowException_WhenFirstNameContainsNumbers() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkFirstName("Max123");
        });
        assert(exception.getMessage().contains("Vorname enthält ungültige Zeichen"));
    }

    /**
     * Tests validation of last name when it is null.
     */
    @Test
    void checkName_ShouldThrowException_WhenNameIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkName(null);
        });
        assert(exception.getMessage().contains("Kein Nachname angegeben"));
    }

    /**
     * Tests validation of last name when it is empty.
     */
    @Test
    void checkName_ShouldThrowException_WhenNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkName("");
        });
        assert(exception.getMessage().contains("Kein Nachname angegeben"));
    }

    /**
     * Tests validation of last name when it contains special characters.
     */
    @Test
    void checkName_ShouldThrowException_WhenNameContainsSpecialChars() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkName("Müller123!");
        });
        assert(exception.getMessage().contains("Nachname enthält ungültige Zeichen"));
    }

    /**
     * Tests validation of birthdate when it is null.
     */
    @Test
    void checkBirthdate_ShouldThrowException_WhenBirthdateIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkBirthdate(null);
        });
        assert(exception.getMessage().contains("Kein Geburtsdatum angegeben"));
    }

    /**
     * Tests validation of birthdate when it is empty.
     */
    @Test
    void checkBirthdate_ShouldThrowException_WhenBirthdateIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkBirthdate("");
        });
        assert(exception.getMessage().contains("Kein Geburtsdatum angegeben"));
    }

    /**
     * Tests validation of birthdate when it has wrong format.
     */
    @Test
    void checkBirthdate_ShouldThrowException_WhenBirthdateHasWrongFormat() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkBirthdate("01.01.1990");
        });
        assert(exception.getMessage().contains("Nicht zulässiges Datumsformat"));
    }

    /**
     * Tests validation of birthdate when it is in the future.
     */
    @Test
    void checkBirthdate_ShouldThrowException_WhenBirthdateIsInFuture() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkBirthdate(LocalDate.now().plusYears(1).toString());
        });
        assert(exception.getMessage().contains("Geburtsdatum liegt in der Zukunft"));
    }

    /**
     * Tests validation of birthdate when person is under 18 years old.
     */
    @Test
    void checkBirthdate_ShouldThrowException_WhenPersonIsUnder18() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkBirthdate(LocalDate.now().minusYears(17).toString());
        });
        assert(exception.getMessage().contains("Sie müssen mindestens 18 Jahre alt sein"));
    }

    /**
     * Tests validation of country when it is null.
     */
    @Test
    void checkCountry_ShouldThrowException_WhenCountryIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkCountry(null);
        });
        assert(exception.getMessage().contains("Kein Land angegeben"));
    }

    /**
     * Tests validation of country when it is empty.
     */
    @Test
    void checkCountry_ShouldThrowException_WhenCountryIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkCountry("");
        });
        assert(exception.getMessage().contains("Kein Land angegeben"));
    }

    /**
     * Tests validation of country when it contains special characters.
     */
    @Test
    void checkCountry_ShouldThrowException_WhenCountryContainsSpecialChars() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkCountry("Deutschland123!");
        });
        assert(exception.getMessage().contains("Land enthält ungültige Zeichen"));
    }

    /**
     * Tests validation of address when it is null.
     */
    @Test
    void checkAddress_ShouldThrowException_WhenAddressIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkAddress(null);
        });
        assert(exception.getMessage().contains("Keine Adresse angegeben"));
    }

    /**
     * Tests validation of address when it is empty.
     */
    @Test
    void checkAddress_ShouldThrowException_WhenAddressIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkAddress("");
        });
        assert(exception.getMessage().contains("Keine Adresse angegeben"));
    }

    /**
     * Tests validation of email when it is null.
     */
    @Test
    void checkEmail_ShouldThrowException_WhenEmailIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkEmail(null);
        });
        assert(exception.getMessage().contains("Keine E-Mail-Adresse angegeben"));
    }

    /**
     * Tests validation of email when it is empty.
     */
    @Test
    void checkEmail_ShouldThrowException_WhenEmailIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkEmail("");
        });
        assert(exception.getMessage().contains("Keine E-Mail-Adresse angegeben"));
    }

    /**
     * Tests validation of email when format is invalid.
     */
    @Test
    void checkEmail_ShouldThrowException_WhenEmailFormatIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkEmail("ungueltigeEmail");
        });
        assert(exception.getMessage().contains("E-Mail-Adresse ist ungültig"));
    }

    /**
     * Tests complete customer validation when first name is invalid.
     */
    @Test
    void evaluateCustomerInfo_ShouldThrowException_WhenFirstNameIsInvalid() {
        Customer customer = new Customer();
        customer.setFirstName("Max123");
        customer.setName("Mustermann");
        customer.setBirthdate("1990-01-01");
        customer.setCountry("Deutschland");
        customer.setAddress("Musterstr. 1");
        customer.setEmail("max@example.com");

        CustomerEvaluator evaluator = new CustomerEvaluator(customer);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            evaluator.evaluateCustomerInfo();
        });
        assert(exception.getMessage().contains("Vorname enthält ungültige Zeichen"));
    }

    /**
     * Tests complete customer validation when last name is invalid.
     */
    @Test
    void evaluateCustomerInfo_ShouldThrowException_WhenLastNameIsInvalid() {
        Customer customer = new Customer();
        customer.setFirstName("Max");
        customer.setName("Mustermann123!");
        customer.setBirthdate("1990-01-01");
        customer.setCountry("Deutschland");
        customer.setAddress("Musterstr. 1");
        customer.setEmail("max@example.com");

        CustomerEvaluator evaluator = new CustomerEvaluator(customer);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            evaluator.evaluateCustomerInfo();
        });
        assert(exception.getMessage().contains("Nachname enthält ungültige Zeichen"));
    }

    /**
     * Tests complete customer validation when birthdate is invalid.
     */
    @Test
    void evaluateCustomerInfo_ShouldThrowException_WhenBirthdateIsInvalid() {
        Customer customer = new Customer();
        customer.setFirstName("Max");
        customer.setName("Mustermann");
        customer.setBirthdate(LocalDate.now().minusYears(17).toString());
        customer.setCountry("Deutschland");
        customer.setAddress("Musterstr. 1");
        customer.setEmail("max@example.com");

        CustomerEvaluator evaluator = new CustomerEvaluator(customer);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            evaluator.evaluateCustomerInfo();
        });
        assert(exception.getMessage().contains("Sie müssen mindestens 18 Jahre alt sein"));
    }

    /**
     * Tests complete customer validation when country is invalid.
     */
    @Test
    void evaluateCustomerInfo_ShouldThrowException_WhenCountryIsInvalid() {
        Customer customer = new Customer();
        customer.setFirstName("Max");
        customer.setName("Mustermann");
        customer.setBirthdate("1990-01-01");
        customer.setCountry("Deutschland123!");
        customer.setAddress("Musterstr. 1");
        customer.setEmail("max@example.com");

        CustomerEvaluator evaluator = new CustomerEvaluator(customer);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            evaluator.evaluateCustomerInfo();
        });
        assert(exception.getMessage().contains("Land enthält ungültige Zeichen"));
    }

    /**
     * Tests complete customer validation when email is invalid.
     */
    @Test
    void evaluateCustomerInfo_ShouldThrowException_WhenEmailIsInvalid() {
        Customer customer = new Customer();
        customer.setFirstName("Max");
        customer.setName("Mustermann");
        customer.setBirthdate("1990-01-01");
        customer.setCountry("Deutschland");
        customer.setAddress("Musterstr. 1");
        customer.setEmail("ungueltigeEmail");

        CustomerEvaluator evaluator = new CustomerEvaluator(customer);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            evaluator.evaluateCustomerInfo();
        });
        assert(exception.getMessage().contains("E-Mail-Adresse ist ungültig"));
    }

    /**
     * Tests complete customer validation when multiple fields are invalid.
     */
    @Test
    void evaluateCustomerInfo_ShouldThrowException_WhenMultipleFieldsAreInvalid() {
        Customer customer = new Customer();
        customer.setFirstName("Max123");
        customer.setName("Mustermann!");
        customer.setBirthdate(LocalDate.now().minusYears(17).toString());
        customer.setCountry("Deutschland123");
        customer.setAddress("");
        customer.setEmail("ungueltig");

        CustomerEvaluator evaluator = new CustomerEvaluator(customer);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            evaluator.evaluateCustomerInfo();
        });
        assert(exception.getMessage().contains("Vorname enthält ungültige Zeichen"));
    }

    /**
     * Tests complete customer validation when all fields are empty.
     */
    @Test
    void evaluateCustomerInfo_ShouldThrowException_WhenAllFieldsAreEmpty() {
        Customer emptyCustomer = new Customer();
        CustomerEvaluator evaluator = new CustomerEvaluator(emptyCustomer);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            evaluator.evaluateCustomerInfo();
        });
        assert(exception.getMessage().contains("Kein Vorname angegeben"));
    }
}