package com.atdit.booking.frontend.controller;

import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.database.DatabaseService;
import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;
import com.atdit.booking.backend.financialdata.financial_information.LiquidAsset;
import com.atdit.booking.backend.financialdata.financial_information.SchufaOverview;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class Page7ControllerPageLoginTest {
    private DatabaseService databaseService;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "Test123!@#";

    @BeforeEach
    void setUp() throws SQLException {
        databaseService = new DatabaseService();
    }

    private Customer createMockCustomer() {
        Customer customer = new Customer();
        customer.setEmail(TEST_EMAIL);
        customer.setTitle("Herr");
        customer.setFirstName("Max");
        customer.setName("Mustermann");
        customer.setCountry("Deutschland");
        customer.setBirthdate("1990-01-01");
        customer.setAddress("Musterstraße 1, 12345 Berlin");

        FinancialInformation financialInfo = new FinancialInformation();
        financialInfo.setAvgNetIncome(3000);
        financialInfo.setMonthlyFixCost(1000);
        financialInfo.setMinCostOfLiving(1200);
        financialInfo.setMonthlyAvailableMoney(800);

        LiquidAsset liquidAsset = new LiquidAsset(
                "DE89370400440532013000",
                "Girokonto",
                1000000, // 10.000 Euro in Cent
                "2024-03-20"
        );
        financialInfo.setProofOfLiquidAssets(liquidAsset);

        // Erstelle SchufaOverview mit Setter-Methoden
        SchufaOverview schufa = new SchufaOverview();
        schufa.setFirstName("Max");
        schufa.setLastName("Mustermann");
        schufa.setScore(97.5);
        schufa.setTotalCredits(1);
        schufa.setTotalCreditSum(50000);
        schufa.setTotalAmountPayed(20000);
        schufa.setTotalAmountOwed(30000);
        schufa.setTotalMonthlyRate(500);
        schufa.setDateIssued("2024-03-20");

        financialInfo.setSchufa(schufa);

        customer.setFinancialInformation(financialInfo);
        return customer;
    }

    @Test
    void getCustomerWithFinancialInfoByEmail_ValidCredentials_ReturnsCustomer() throws SQLException {
        // Arrange
        Customer expectedCustomer = createMockCustomer();
        DatabaseService mockDatabaseService = new DatabaseService() {
            @Override
            public Customer getCustomerWithFinancialInfoByEmail(String email, String password) throws SQLException {
                if (email.equals(TEST_EMAIL) && password.equals(TEST_PASSWORD)) {
                    return expectedCustomer;
                }
                throw new SQLException("Ungültige Anmeldedaten");
            }
        };

        // Act
        Customer retrievedCustomer = mockDatabaseService.getCustomerWithFinancialInfoByEmail(TEST_EMAIL, TEST_PASSWORD);

        // Assert
        assertNotNull(retrievedCustomer);
        assertEquals(TEST_EMAIL, retrievedCustomer.getEmail());
        assertEquals("Max", retrievedCustomer.getFirstName());
        assertEquals("Mustermann", retrievedCustomer.getName());
    }

    @Test
    void getCustomerWithFinancialInfoByEmail_EmptyEmail_ThrowsException() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                databaseService.getCustomerWithFinancialInfoByEmail("", TEST_PASSWORD)
        );
    }

    @Test
    void getCustomerWithFinancialInfoByEmail_EmptyPassword_ThrowsException() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                databaseService.getCustomerWithFinancialInfoByEmail(TEST_EMAIL, "")
        );
    }

    @Test
    void getCustomerWithFinancialInfoByEmail_NonexistentEmail_ThrowsException() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                databaseService.getCustomerWithFinancialInfoByEmail("nonexistent@example.com", TEST_PASSWORD)
        );
    }

    @AfterEach
    void tearDown() {
        databaseService.closeConnection();
    }
}