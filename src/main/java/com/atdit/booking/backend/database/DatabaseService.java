package com.atdit.booking.backend.database;

import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.exceptions.DecryptionException;
import com.atdit.booking.backend.exceptions.EncryptionException;
import com.atdit.booking.backend.exceptions.HashingException;
import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;
import com.atdit.booking.backend.financialdata.financial_information.IncomeProof;
import com.atdit.booking.backend.financialdata.financial_information.LiquidAsset;
import com.atdit.booking.backend.financialdata.financial_information.SchufaOverview;

import java.sql.*;



public class DatabaseService {

    private static final String DB_URL = "jdbc:sqlite:database/customers.db";
    private static final Encrypter encrypter = new Encrypter();
    private final Connection connection;
    private Customer currentCustomer;


    public DatabaseService() throws SQLException {

        try{
            this.connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new SQLException("Failed to establish a connection to the database", e);
        }
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }


    private long insertIncomeProof() throws SQLException {

        IncomeProof proof = this.currentCustomer.getFinancialInformation().getProofOfIncome();

        if (proof == null) {
            return 0;
        }

        String sql = """
        INSERT INTO income_proofs (monthlyNetIncome, employer, employmentType, 
        employmentDurationMonths, dateIssued)
        VALUES (?, ?, ?, ?, ?)
        """;

        PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setInt(1, proof.monthlyNetIncome());
        pstmt.setString(2, proof.employer());
        pstmt.setString(3, proof.employmentType());
        pstmt.setInt(4, proof.employmentDurationMonths());
        pstmt.setString(5, proof.dateIssued());

        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();

        if (rs.next()){
            return rs.getLong(1);
        }

        throw new SQLException("Failed to save proof of income");

    }

    private long insertAssetsProof() throws SQLException {

        String sql = """
        INSERT INTO liquid_assets (iban, description, balance, dateIssued)
        VALUES (?, ?, ?, ?)
        """;

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

    }

    private long insertSchufaRecord() throws SQLException {

        SchufaOverview schufa = this.currentCustomer.getFinancialInformation().getSchufa();

        String sql = """
        INSERT INTO schufa_overview (firstName, lastName, score, totalCredits,
        totalCreditSum, totalAmountPayed, totalAmountOwed, totalMonthlyRate, dateIssued)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

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
    }


    private void insertCustomerData(long financialInfoId, String password) throws SQLException, EncryptionException, HashingException {

        String email = this.currentCustomer.getEmail();

        String sql = """
    INSERT INTO customers (email_hashed, email, title, firstName, name, country, birthdate, address, financial_info_id)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;


            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, encrypter.hashString(email));
            pstmt.setString(2, encrypter.encrypt(email, email, password));
            pstmt.setString(3, encrypter.encrypt(this.currentCustomer.getTitle(), email, password));
            pstmt.setString(4, encrypter.encrypt(this.currentCustomer.getFirstName(), email, password));
            pstmt.setString(5, encrypter.encrypt(this.currentCustomer.getName(), email, password));
            pstmt.setString(6, encrypter.encrypt(this.currentCustomer.getCountry(), email, password));
            pstmt.setString(7, encrypter.encrypt(this.currentCustomer.getBirthdate(), email, password));
            pstmt.setString(8, encrypter.encrypt(this.currentCustomer.getAddress(), email, password));
            pstmt.setLong(9, financialInfoId);
            pstmt.executeUpdate();


    }

    private long insertFinancialData(long incomeProofId, long assetsProofId, long schufaId) throws SQLException {

        FinancialInformation financialInfo = this.currentCustomer.getFinancialInformation();

        System.out.println("Financial Info: " + financialInfo);

        String sql = """
        INSERT INTO financial_information
        (avgNetIncome, liquidAssets, monthlyFixCost, minCostOfLiving,
        monthlyAvailableMoney, proof_of_income_id, proof_of_liquid_assets_id, schufa_id)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

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

            System.out.println(id);
            return id;
        }

        throw new SQLException("Failed to save financial information");
    }


    public void saveCustomerInDatabase(String password) throws SQLException, RuntimeException {

        long incomeProofId = insertIncomeProof();
        long assetsProofId = insertAssetsProof();
        long schufaId = insertSchufaRecord();
        long financialInfoId = insertFinancialData(incomeProofId, assetsProofId, schufaId);

        try{
            insertCustomerData(financialInfoId, password);
        }
        catch(EncryptionException | HashingException e){
            throw new RuntimeException("Failed to encrypt customer data", e);
        }

        closeConnection();
    }

    public void checkIfCustomerIsInDatabase(String email) throws RuntimeException {


        String sql = "Select * FROM customers WHERE email_hashed = ?";

        try{
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, encrypter.hashString(email));
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                throw new IllegalArgumentException("Es existiert bereits ein Konto mit der angegebenen E-Mail-Adresse.");
            }
        }
        catch(SQLException | HashingException ex){
            throw new IllegalArgumentException("Fehler beim abrufen der Daten.");
        }

    }

    public Customer getCustomerWithFinancialInfoByEmail(String email, String password) throws SQLException, RuntimeException {

        String sql = "SELECT c.*, fi.*, ip.*, la.*, so.* " +
                "FROM customers c " +
                "JOIN financial_information fi ON c.financial_info_id = fi.id " +
                "LEFT JOIN income_proofs ip ON fi.proof_of_income_id = ip.id " +
                "LEFT JOIN liquid_assets la ON fi.proof_of_liquid_assets_id = la.id " +
                "LEFT JOIN schufa_overview so ON fi.schufa_id = so.id " +
                "WHERE c.email_hashed = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, encrypter.hashString(email));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                Customer customer = extractCustomerFromResultSet(rs, email, password);
                //FinancialInformation financialInfo = extractFinancialInfoFromResultSet(rs);
                //IncomeProof incomeProof = extractIncomeProofFromResultSet(rs);
                //LiquidAsset liquidAsset = extractLiquidAssetFromResultSet(rs);
                //SchufaOverview schufaOverview = extractSchufaOverviewFromResultSet(rs);

                //financialInfo.setProofOfIncome(incomeProof);
                //financialInfo.setProofOfLiquidAssets(liquidAsset);
                //financialInfo.setSchufa(schufaOverview);

                //customer.setFinancialInformation(financialInfo);
                customer.setFinancialInformation(null);

                return customer;
            }
            return null;
        }
        catch (SQLException e) {
            throw new SQLException("Failed to retrieve customer data", e);
        }
        catch (DecryptionException | HashingException e){
            throw new RuntimeException("Failed to decrypt customer data", e);
        }
        catch (Exception e){
            throw new RuntimeException("Failed to retrieve customer data " + e.getMessage(), e);
        }

    }

    private Customer extractCustomerFromResultSet(ResultSet rs, String email, String password) throws Exception {

        Customer customer = new Customer();

        customer.setEmail(email);
        customer.setTitle(encrypter.decrypt(rs.getString("title"), email, password));
        customer.setFirstName(encrypter.decrypt(rs.getString("firstName"), email, password));
        customer.setName(encrypter.decrypt(rs.getString("name"), email, password));
        customer.setCountry(encrypter.decrypt(rs.getString("country"), email, password));
        customer.setBirthdate(encrypter.decrypt(rs.getString("birthdate"), email, password));
        customer.setAddress(encrypter.decrypt(rs.getString("address"), email, password));

        return customer;
    }


    private FinancialInformation extractFinancialInfoFromResultSet(ResultSet rs) throws SQLException {

        FinancialInformation financialInfo = new FinancialInformation();

        financialInfo.setAvgNetIncome(rs.getInt("avgNetIncome"));
        financialInfo.setLiquidAssets(rs.getInt("liquidAssets"));
        financialInfo.setMonthlyFixCost(rs.getInt("monthlyFixCost"));
        financialInfo.setMinCostOfLiving(rs.getInt("minCostOfLiving"));
        financialInfo.setMonthlyAvailableMoney(rs.getInt("monthlyAvailableMoney"));

        return financialInfo;
    }


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


    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error closing database connection", e);
        }
    }



    private void createTables() throws SQLException {
        String[] tables = {
                """
        CREATE TABLE IF NOT EXISTS customers (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            email TEXT ,
            email_hashed TEXT,
            title TEXT,
            firstName TEXT,
            name TEXT,
            country TEXT,
            birthdate TEXT,
            address TEXT,
            financial_info_id INTEGER,
            FOREIGN KEY (financial_info_id) REFERENCES financial_information(id)
        )""",

                """
        CREATE TABLE IF NOT EXISTS income_proofs (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            monthlyNetIncome INTEGER,
            employer TEXT,
            employmentType TEXT,
            employmentDurationMonths INTEGER,
            dateIssued TEXT
        )""",

                """
        CREATE TABLE IF NOT EXISTS liquid_assets (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            iban TEXT,
            description TEXT,
            balance INTEGER,
            dateIssued TEXT
        )""",

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

        """
        CREATE TABLE IF NOT EXISTS customer_progress (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
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
    }

    public void validatePasswords(String password, String confirm) throws ValidationException{

        boolean isValid = password.equals(confirm) &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?].*");

        if(!isValid){
            throw new ValidationException("Passwort erfüllt nicht die Anforderungen");
        }
    }

}