package com.atdit.booking.frontend.controller;

import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.customer.CustomerEvaluator;
import com.atdit.booking.backend.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class Page3PersonalInformationControllerTest {
    private CustomerEvaluator evaluator;
    private Customer validCustomer;

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

    @Test
    void checkFirstName_ShouldThrowException_WhenFirstNameIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkFirstName(null);
        });
        assert(exception.getMessage().contains("Kein Vorname angegeben"));
    }

    @Test
    void checkFirstName_ShouldThrowException_WhenFirstNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkFirstName("");
        });
        assert(exception.getMessage().contains("Kein Vorname angegeben"));
    }

    @Test
    void checkFirstName_ShouldThrowException_WhenFirstNameContainsNumbers() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkFirstName("Max123");
        });
        assert(exception.getMessage().contains("Vorname enthält ungültige Zeichen"));
    }

    @Test
    void checkName_ShouldThrowException_WhenNameIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkName(null);
        });
        assert(exception.getMessage().contains("Kein Nachname angegeben"));
    }

    @Test
    void checkName_ShouldThrowException_WhenNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkName("");
        });
        assert(exception.getMessage().contains("Kein Nachname angegeben"));
    }

    @Test
    void checkName_ShouldThrowException_WhenNameContainsSpecialChars() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkName("Müller123!");
        });
        assert(exception.getMessage().contains("Nachname enthält ungültige Zeichen"));
    }

    @Test
    void checkBirthdate_ShouldThrowException_WhenBirthdateIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkBirthdate(null);
        });
        assert(exception.getMessage().contains("Kein Geburtsdatum angegeben"));
    }

    @Test
    void checkBirthdate_ShouldThrowException_WhenBirthdateIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkBirthdate("");
        });
        assert(exception.getMessage().contains("Kein Geburtsdatum angegeben"));
    }

    @Test
    void checkBirthdate_ShouldThrowException_WhenBirthdateHasWrongFormat() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkBirthdate("01.01.1990");
        });
        assert(exception.getMessage().contains("Nicht zulässiges Datumsformat"));
    }

    @Test
    void checkBirthdate_ShouldThrowException_WhenBirthdateIsInFuture() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkBirthdate(LocalDate.now().plusYears(1).toString());
        });
        assert(exception.getMessage().contains("Geburtsdatum liegt in der Zukunft"));
    }

    @Test
    void checkBirthdate_ShouldThrowException_WhenPersonIsUnder18() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkBirthdate(LocalDate.now().minusYears(17).toString());
        });
        assert(exception.getMessage().contains("Sie müssen mindestens 18 Jahre alt sein"));
    }

    @Test
    void checkCountry_ShouldThrowException_WhenCountryIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkCountry(null);
        });
        assert(exception.getMessage().contains("Kein Land angegeben"));
    }

    @Test
    void checkCountry_ShouldThrowException_WhenCountryIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkCountry("");
        });
        assert(exception.getMessage().contains("Kein Land angegeben"));
    }

    @Test
    void checkCountry_ShouldThrowException_WhenCountryContainsSpecialChars() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkCountry("Deutschland123!");
        });
        assert(exception.getMessage().contains("Land enthält ungültige Zeichen"));
    }

    @Test
    void checkAddress_ShouldThrowException_WhenAddressIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkAddress(null);
        });
        assert(exception.getMessage().contains("Keine Adresse angegeben"));
    }

    @Test
    void checkAddress_ShouldThrowException_WhenAddressIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkAddress("");
        });
        assert(exception.getMessage().contains("Keine Adresse angegeben"));
    }

    @Test
    void checkEmail_ShouldThrowException_WhenEmailIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkEmail(null);
        });
        assert(exception.getMessage().contains("Keine E-Mail-Adresse angegeben"));
    }

    @Test
    void checkEmail_ShouldThrowException_WhenEmailIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkEmail("");
        });
        assert(exception.getMessage().contains("Keine E-Mail-Adresse angegeben"));
    }

    @Test
    void checkEmail_ShouldThrowException_WhenEmailFormatIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkEmail("ungueltigeEmail");
        });
        assert(exception.getMessage().contains("E-Mail-Adresse ist ungültig"));
    }

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