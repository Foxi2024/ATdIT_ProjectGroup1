package com.atdit.booking.frontend.controllers.payment_process;

import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.database.DatabaseService;
import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;
import com.atdit.booking.backend.financialdata.financial_information.LiquidAsset;
import com.atdit.booking.backend.financialdata.financial_information.SchufaOverview;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Page7Controller login functionality.
 * Tests the database operations related to customer authentication and retrieval.
 */
class Page1ControllerPageLoginTest {
    private DatabaseService databaseService;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "Test123!@#";

    /**
     * Sets up the test environment before each test.
     * Initializes a new DatabaseService instance.
     *
     * @throws SQLException if database connection fails
     */
    @BeforeEach
    void setUp() throws SQLException {
        databaseService = new DatabaseService();
    }

    /**
     * Creates a mock Customer object with predefined test data.
     * Includes personal information, financial data, liquid assets, and SCHUFA information.
     *
     * @return Customer object populated with mock data
     */
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

        // Create SchufaOverview using setter methods
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

    /**
     * Tests customer retrieval with valid credentials.
     * Verifies that the correct customer data is returned when valid email and password are provided.
     *
     * @throws SQLException if database operation fails
     */
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

    /**
     * Tests that an empty email address throws an IllegalArgumentException.
     */
    @Test
    void getCustomerWithFinancialInfoByEmail_EmptyEmail_ThrowsException() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                databaseService.getCustomerWithFinancialInfoByEmail("", TEST_PASSWORD)
        );
    }

    /**
     * Tests that an empty password throws an IllegalArgumentException.
     */
    @Test
    void getCustomerWithFinancialInfoByEmail_EmptyPassword_ThrowsException() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                databaseService.getCustomerWithFinancialInfoByEmail(TEST_EMAIL, "")
        );
    }

    /**
     * Tests that using a non-existent email address throws an IllegalArgumentException.
     */
    @Test
    void getCustomerWithFinancialInfoByEmail_NonexistentEmail_ThrowsException() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                databaseService.getCustomerWithFinancialInfoByEmail("nonexistent@example.com", TEST_PASSWORD)
        );
    }

    /**
     * Cleans up resources after each test.
     * Closes the database connection.
     */
    @AfterEach
    void tearDown() {
        databaseService.closeConnection();
    }
}