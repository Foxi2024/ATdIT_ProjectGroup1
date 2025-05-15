package com.atdit.booking.backend.database;

import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.exceptions.*;
import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;
import com.atdit.booking.backend.financialdata.financial_information.IncomeProof;
import com.atdit.booking.backend.financialdata.financial_information.LiquidAsset;
import com.atdit.booking.backend.financialdata.financial_information.SchufaOverview;

import java.sql.*;

/**
 * Service class for handling all database operations related to customer data.
 * Manages connections to SQLite database and provides CRUD operations for customer information.
 */
public class DatabaseService {

    /**
     * URL for the SQLite database connection
     */
    private static final String DB_URL = "jdbc:sqlite:database/customers.db";

    /**
     * Encrypter instance for handling encryption/decryption of sensitive data
     */
    private static final Encrypter encrypter = new Encrypter();

    /**
     * Currently active customer being processed
     */
    private Customer currentCustomer;

    /**
     * Constructs a new DatabaseService
     * Initializes the service, note that database tables are created on demand if they don't exist
     * when other methods like `saveCustomerInDatabase` are called, which internally call `createTables`
     */
    public DatabaseService() {
        // Constructor can be used to initialize any specific settings if needed in the future
        // Table creation is handled by createTables() method, called when data operations are performed
    }

    /**
     * Establishes and returns a connection to the SQLite database
     *
     * @return A Connection object to the database
     * @throws SQLException if database connection fails
     */
    private Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL);
        }
        catch (SQLException e) {
            throw new SQLException("Verbindung zur Datenbank fehlgeschlagen.", e);
        }
    }

    /**
     * Sets the current customer for database operations.
     *
     * @param currentCustomer the customer to be set as current
     */
    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    /**
     * Inserts income proof information into the database.
     * Creates a new record in the income_proofs table with the current customer's income information.
     *
     * @return generated ID of the inserted income proof, or 0 if no proof exists
     * @throws SQLException if database operation fails
     */
    private long insertIncomeProof() throws SQLException {

        Connection connection = null;

        IncomeProof proof = this.currentCustomer.getFinancialInformation().getProofOfIncome();

        if (proof == null) {
            return 0;
        }

        String sql = """
                INSERT INTO income_proofs (monthlyNetIncome, employer, employmentType,
                employmentDurationMonths, dateIssued)
                VALUES (?, ?, ?, ?, ?)
                """;


        try {
            connection = getConnection();

            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, proof.monthlyNetIncome());
            pstmt.setString(2, proof.employer());
            pstmt.setString(3, proof.employmentType());
            pstmt.setInt(4, proof.employmentDurationMonths());
            pstmt.setString(5, proof.dateIssued());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new SQLException("Failed to save proof of income, no generated key found");
        } catch (SQLException ex) {
            throw new SQLException("Failed to save proof of income: " + ex.getMessage(), ex);
        } finally {
            closeConnection(connection);
        }


    }

    /**
     * Inserts liquid assets proof into the database.
     * Creates a new record in the liquid_assets table with the current customer's asset information.
     *
     * @return generated ID of the inserted assets proof
     * @throws SQLException if database operation fails
     */
    private long insertAssetsProof() throws SQLException {


        Connection connection = null;

        String sql = """
                INSERT INTO liquid_assets (iban, description, balance, dateIssued)
                VALUES (?, ?, ?, ?)
                """;

        try {
            connection = getConnection();

            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            LiquidAsset proof = this.currentCustomer.getFinancialInformation().getProofOfLiquidAssets();
            pstmt.setString(1, proof.iban());
            pstmt.setString(2, proof.description());
            pstmt.setInt(3, proof.balance());
            pstmt.setString(4, proof.dateIssued());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            }

            throw new SQLException("Failed to save proof of liquid assets");
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * Inserts SCHUFA record into the database.
     * Creates a new record in the schufa_overview table with the current customer's SCHUFA information.
     *
     * @return generated ID of the inserted SCHUFA record
     * @throws SQLException if database operation fails
     */
    private long insertSchufaRecord() throws SQLException {

        Connection connection = null;

        SchufaOverview schufa = this.currentCustomer.getFinancialInformation().getSchufa();

        String sql = """
                INSERT INTO schufa_overview (firstName, lastName, score, totalCredits,
                totalCreditSum, totalAmountPayed, totalAmountOwed, totalMonthlyRate, dateIssued)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try {

            connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, schufa.getFirstName());
            pstmt.setString(2, schufa.getLastName());
            pstmt.setDouble(3, schufa.getScore());
            pstmt.setInt(4, schufa.getTotalCredits());
            pstmt.setInt(5, schufa.getTotalCreditSum());
            pstmt.setInt(6, schufa.getTotalAmountPayed());
            pstmt.setInt(7, schufa.getTotalAmountOwed());
            pstmt.setInt(8, schufa.getTotalMonthlyRate());
            pstmt.setString(9, schufa.getDateIssued());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new SQLException("Failed to save schufa overview");
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * Inserts customer personal data into the database.
     * Creates a new record in the customers table with encrypted personal information.
     *
     * @param financialInfoId ID of the associated financial information
     * @param password        customer's password for encryption
     * @throws SQLException        if database operation fails
     * @throws EncryptionException if data encryption fails
     * @throws HashingException    if email hashing fails
     */
    private void insertCustomerData(long financialInfoId, String password) throws SQLException, EncryptionException, HashingException {
        Connection connection = null;

        String email = this.currentCustomer.getEmail();

        String sql = """
                INSERT INTO customers (email_hashed, email, title, firstName, name, country, birthdate, address, financial_info_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try {
            connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, encrypter.hashString(email));
            pstmt.setString(2, encrypter.encrypt(email, email, password));
            pstmt.setString(3, encrypter.encrypt(this.currentCustomer.getTitle(), email, password));
            pstmt.setString(4, encrypter.encrypt(this.currentCustomer.getFirstName(), email, password));
            pstmt.setString(5, encrypter.encrypt(this.currentCustomer.getLastName(), email, password));
            pstmt.setString(6, encrypter.encrypt(this.currentCustomer.getCountry(), email, password));
            pstmt.setString(7, encrypter.encrypt(this.currentCustomer.getBirthdate(), email, password));
            pstmt.setString(8, encrypter.encrypt(this.currentCustomer.getAddress(), email, password));
            pstmt.setLong(9, financialInfoId);
            pstmt.executeUpdate();

        } finally {
            closeConnection(connection);
        }
    }

    /**
     * Inserts financial information into the database.
     * Creates a new record in the financial_information table linking all financial proofs.
     *
     * @param incomeProofId ID of the associated income proof
     * @param assetsProofId ID of the associated assets proof
     * @param schufaId      ID of the associated SCHUFA record
     * @return generated ID of the inserted financial information
     * @throws SQLException if database operation fails
     */
    private long insertFinancialData(long incomeProofId, long assetsProofId, long schufaId) throws SQLException {

        Connection connection = null;

        FinancialInformation financialInfo = this.currentCustomer.getFinancialInformation();

        String sql = """
                INSERT INTO financial_information
                (avgNetIncome, liquidAssets, monthlyFixCost, minCostOfLiving,
                monthlyAvailableMoney, proof_of_income_id, proof_of_liquid_assets_id, schufa_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try {
            connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, financialInfo.getAvgNetIncome());
            pstmt.setInt(2, financialInfo.getLiquidAssets());
            pstmt.setInt(3, financialInfo.getMonthlyFixCost());
            pstmt.setInt(4, financialInfo.getMinCostOfLiving());
            pstmt.setInt(5, financialInfo.getMonthlyAvailableMoney());
            pstmt.setLong(6, incomeProofId);
            pstmt.setLong(7, assetsProofId);
            pstmt.setLong(8, schufaId);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                long id = rs.getLong(1);
                return id;
            }

            throw new SQLException("Failed to save financial information");
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * Saves complete customer information in the database.
     * Coordinates the insertion of all customer-related data including financial proofs.
     *
     * @param password customer's password for encryption
     * @throws SQLException     if database operation fails
     * @throws RuntimeException if encryption/hashing fails
     */
    public void saveCustomerInDatabase(String password) throws SQLException, RuntimeException {
        long incomeProofId = insertIncomeProof();
        long assetsProofId = insertAssetsProof();
        long schufaId = insertSchufaRecord();
        long financialInfoId = insertFinancialData(incomeProofId, assetsProofId, schufaId);

        try {
            insertCustomerData(financialInfoId, password);
        } catch (EncryptionException | HashingException e) {
            throw new RuntimeException("Failed to encrypt customer data", e);
        }
    }

    /**
     * Checks if a customer with given email already exists in database.
     *
     * @param email email to check
     * @throws RuntimeException         if check fails
     * @throws IllegalArgumentException if customer already exists
     */
    public void checkIfCustomerIsInDatabase(String email) throws RuntimeException, IllegalArgumentException {
        Connection connection = null;
        try {
            connection = getConnection();
            String sql = "Select * FROM customers WHERE email_hashed = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, encrypter.hashString(email));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                throw new IllegalArgumentException("Es existiert bereits ein Konto mit der angegebenen E-Mail-Adresse.");
            }
        } catch (SQLException | HashingException ex) {
            throw new RuntimeException("Fehler beim abrufen der Daten.");
        }
        finally {
            closeConnection(connection);
        }
    }

    /**
     * Retrieves customer and their financial information by email.
     *
     * @param email    customer's email
     * @param password customer's password for decryption
     * @return Customer object with associated data
     * @throws IllegalArgumentException if credentials are invalid
     * @throws SQLException             if database operation fails
     * @throws CryptographyException    if decryption fails
     */
    public Customer getCustomerWithFinancialInfoByEmail(String email, String password) throws IllegalArgumentException, SQLException, CryptographyException {
        Connection connection = null;

        if (email.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Email- oder Passwortfeld ist leer.");
        }

        String sql = "SELECT c.*, fi.*, ip.*, la.*, so.* " +
                "FROM customers c " +
                "JOIN financial_information fi ON c.financial_info_id = fi.id " +
                "LEFT JOIN income_proofs ip ON fi.proof_of_income_id = ip.id " +
                "LEFT JOIN liquid_assets la ON fi.proof_of_liquid_assets_id = la.id " +
                "LEFT JOIN schufa_overview so ON fi.schufa_id = so.id " +
                "WHERE c.email_hashed = ?";

        try {
            connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, encrypter.hashString(email));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Customer customer = extractCustomerFromResultSet(rs, email, password);
                FinancialInformation financialInfo = extractFinancialInfoFromResultSet(rs);

                long incomeProofId = rs.getLong("proof_of_income_id");
                if (!rs.wasNull()) {
                    financialInfo.setProofOfIncome(extractIncomeProofFromResultSet(rs));
                }

                long liquidAssetsProofId = rs.getLong("proof_of_liquid_assets_id");
                if (!rs.wasNull()) {
                    financialInfo.setProofOfLiquidAssets(extractLiquidAssetFromResultSet(rs));
                }

                long schufaId = rs.getLong("schufa_id");
                if (!rs.wasNull()) {
                    financialInfo.setSchufa(extractSchufaOverviewFromResultSet(rs));
                }

                customer.setFinancialInformation(financialInfo);

                return customer;
            }

            throw new IllegalArgumentException("E-Mail oder Passwort falsch.");
        } catch (SQLException ex) {
            throw new SQLException("Fehler beim Verbindungsaufbau mit der Datenbank.");
        } catch (HashingException | DecryptionException ex) {
            throw new CryptographyException("Entschlüsslung oder Hashing der Daten ist fehlgeschlagen.");
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * Extracts customer information from database result set.
     *
     * @param rs       ResultSet containing customer data
     * @param email    customer's email for decryption
     * @param password customer's password for decryption
     * @return populated Customer object
     * @throws SQLException        if data extraction fails
     * @throws DecryptionException if data decryption fails
     */
    private Customer extractCustomerFromResultSet(ResultSet rs, String email, String password) throws SQLException, DecryptionException {
        Customer customer = new Customer();

        customer.setEmail(email);
        customer.setTitle(encrypter.decrypt(rs.getString("title"), email, password));
        customer.setFirstName(encrypter.decrypt(rs.getString("firstName"), email, password));
        customer.setLastName(encrypter.decrypt(rs.getString("name"), email, password));
        customer.setCountry(encrypter.decrypt(rs.getString("country"), email, password));
        customer.setBirthdate(encrypter.decrypt(rs.getString("birthdate"), email, password));
        customer.setAddress(encrypter.decrypt(rs.getString("address"), email, password));

        return customer;
    }

    /**
     * Extracts financial information from database result set.
     *
     * @param rs ResultSet containing financial data
     * @return populated FinancialInformation object
     * @throws SQLException if data extraction fails
     */
    private FinancialInformation extractFinancialInfoFromResultSet(ResultSet rs) throws SQLException {
        FinancialInformation financialInfo = new FinancialInformation();

        financialInfo.setAvgNetIncome(rs.getInt("avgNetIncome"));
        financialInfo.setLiquidAssets(rs.getInt("liquidAssets"));
        financialInfo.setMonthlyFixCost(rs.getInt("monthlyFixCost"));
        financialInfo.setMinCostOfLiving(rs.getInt("minCostOfLiving"));
        financialInfo.setMonthlyAvailableMoney(rs.getInt("monthlyAvailableMoney"));

        return financialInfo;
    }

    /**
     * Extracts income proof from database result set.
     *
     * @param rs ResultSet containing income proof data
     * @return populated IncomeProof object or null if no data
     * @throws SQLException if data extraction fails
     */
    private IncomeProof extractIncomeProofFromResultSet(ResultSet rs) throws SQLException {
        if (rs.wasNull()) {
            return null;
        }
        return new IncomeProof(
                rs.getInt("monthlyNetIncome"),
                rs.getString("employer"),
                rs.getString("employmentType"),
                rs.getInt("employmentDurationMonths"),
                rs.getString("dateIssued")
        );
    }

    /**
     * Extracts liquid asset information from database result set.
     *
     * @param rs ResultSet containing liquid asset data
     * @return populated LiquidAsset object or null if no data
     * @throws SQLException if data extraction fails
     */
    private LiquidAsset extractLiquidAssetFromResultSet(ResultSet rs) throws SQLException {
        if (rs.wasNull()) {
            return null;
        }
        return new LiquidAsset(
                rs.getString("iban"),
                rs.getString("description"),
                rs.getInt("balance"),
                rs.getString("dateIssued")
        );
    }

    /**
     * Extracts SCHUFA overview from database result set.
     *
     * @param rs ResultSet containing SCHUFA data
     * @return populated SchufaOverview object or null if no data
     * @throws SQLException if data extraction fails
     */
    private SchufaOverview extractSchufaOverviewFromResultSet(ResultSet rs) throws SQLException {
        if (rs.wasNull()) {
            return null;
        }
        return new SchufaOverview(
                rs.getDouble("score"),
                rs.getInt("totalCredits"),
                rs.getInt("totalCreditSum"),
                rs.getInt("totalAmountPayed"),
                rs.getInt("totalAmountOwed"),
                rs.getInt("totalMonthlyRate"),
                rs.getString("dateIssued")
        );
    }

    /**
     * Closes the database connection.
     *
     * @throws RuntimeException if closing connection fails
     */
    public void closeConnection(Connection connection) throws RuntimeException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Datenbankverbindung konnte nicht geschlossen werden.", e);
        }
    }

    /**
     * Creates all necessary database tables if they don't exist.
     * Initializes the database schema with required tables for storing customer and financial data.
     *
     * @throws SQLException if table creation fails
     */
    private void createTables() throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            String[] tables = {
                    // Stores main customer details, with sensitive information encrypted
                    // Links to financial_information table
                    """
        CREATE TABLE IF NOT EXISTS customers (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            email TEXT ,
            email_hashed TEXT,            -- Hashed email for quick lookups without decryption
            title TEXT,                   -- Encrypted
            firstName TEXT,               -- Encrypted
            name TEXT,                    -- Encrypted (Last Name)
            country TEXT,                 -- Encrypted
            birthdate TEXT,               -- Encrypted
            address TEXT,                 -- Encrypted
            financial_info_id INTEGER,
            FOREIGN KEY (financial_info_id) REFERENCES financial_information(id)
        )""",

                    // Stores details from the income proof document
                    """
        CREATE TABLE IF NOT EXISTS income_proofs (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            monthlyNetIncome INTEGER,
            employer TEXT,
            employmentType TEXT,
            employmentDurationMonths INTEGER,
            dateIssued TEXT
        )""",

                    // Stores details from the liquid assets proof document
                    """
        CREATE TABLE IF NOT EXISTS liquid_assets (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            iban TEXT,
            description TEXT,
            balance INTEGER,
            dateIssued TEXT
        )""",

                    // Stores overview data from the Schufa report
                    """
        CREATE TABLE IF NOT EXISTS schufa_overview (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            firstName TEXT,
            lastName TEXT,
            score REAL,
            totalCredits INTEGER,
            totalCreditSum INTEGER,
            totalAmountPayed INTEGER,
            totalAmountOwed INTEGER,
            totalMonthlyRate INTEGER,
            dateIssued TEXT
        )""",

                    // Central table linking customer to their various financial proofs and summary data
                    """
        CREATE TABLE IF NOT EXISTS financial_information (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            avgNetIncome INTEGER,
            liquidAssets INTEGER,
            monthlyFixCost INTEGER,
            minCostOfLiving INTEGER,
            monthlyAvailableMoney INTEGER,
            proof_of_income_id INTEGER,
            proof_of_liquid_assets_id INTEGER,
            schufa_id INTEGER,
            FOREIGN KEY (proof_of_income_id) REFERENCES income_proofs(id),
            FOREIGN KEY (proof_of_liquid_assets_id) REFERENCES liquid_assets(id),
            FOREIGN KEY (schufa_id) REFERENCES schufa_overview(id)
        )""",

                    // Could be used to track customer progress through a multi-step process (e.g., registration)
                    """
        CREATE TABLE IF NOT EXISTS customer_progress (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            customer_id INTEGER,
            FOREIGN KEY (customer_id) REFERENCES customers(id),
            current_step TEXT
        )
        """
            };

            try (Statement stmt = connection.createStatement()) {
                for (String sql : tables) {
                    stmt.execute(sql);
                }
            }
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * Validates password against security requirements.
     * Checks if password meets minimum length, contains uppercase, lowercase,
     * digits and special characters, and matches confirmation.
     *
     * @param password password to validate
     * @param confirm  password confirmation to match
     * @throws ValidationException if password requirements are not met
     */
    public void validatePasswords(String password, String confirm) throws ValidationException {

        // System.out.println(password); // Debug statement removed
        // System.out.println(confirm);  // Debug statement removed

        boolean isValid = password != null &&
                password.equals(confirm) &&
                password.length() >= 8 && // Check for minimum length (at least 8 characters)
                password.matches(".*[A-Z].*") && // Check for at least one uppercase letter
                password.matches(".*[a-z].*") && // Check for at least one lowercase letter
                password.matches(".*\\d.*") && // Check for at least one digit
                // Check for at least one special character from the predefined set:
                // !@#$%^&*()\-_=+|\\[{\\]};:'\",<.>/?
                password.matches(".*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?].*");

        if (!isValid) {
            throw new ValidationException("Passwort erfüllt nicht die Anforderungen");
        }
    }
}