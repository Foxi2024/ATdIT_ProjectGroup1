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
    void checkFirstName_ShouldThrowException_WhenFirstNameContainsNumbers() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkFirstName("Max123");
        });
        assert(exception.getMessage().contains("Vorname enthält ungültige Zeichen"));
    }

    @Test
    void checkName_ShouldThrowException_WhenNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkName("");
        });
        assert(exception.getMessage().contains("Kein Nachname angegeben"));
    }

    @Test
    void checkBirthdate_ShouldThrowException_WhenPersonIsUnder18() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkBirthdate(LocalDate.now().minusYears(16).toString());
        });
        assert(exception.getMessage().contains("Sie müssen mindestens 18 Jahre alt sein"));
    }

    @Test
    void checkCountry_ShouldThrowException_WhenCountryContainsSpecialChars() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkCountry("Deutschland!");
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
    void checkEmail_ShouldThrowException_WhenEmailFormatIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            evaluator.checkEmail("ungueltigeEmail");
        });
        assert(exception.getMessage().contains("E-Mail-Adresse ist ungültig"));
    }

    @Test
    void evaluateCustomerInfo_ShouldThrowException_WhenAnyValidationFails() {
        Customer invalidCustomer = new Customer();
        invalidCustomer.setFirstName("Max123");
        invalidCustomer.setName("Mustermann");
        invalidCustomer.setBirthdate("1990-01-01");
        invalidCustomer.setCountry("Deutschland");
        invalidCustomer.setAddress("Musterstr. 1");
        invalidCustomer.setEmail("max@example.com");
        CustomerEvaluator evaluator = new CustomerEvaluator(invalidCustomer);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            evaluator.evaluateCustomerInfo();
        });
        assert(exception.getMessage().contains("Vorname enthält ungültige Zeichen"));
    }
}