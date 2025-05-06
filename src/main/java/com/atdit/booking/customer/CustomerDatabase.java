package com.atdit.booking.customer;

import com.atdit.booking.Main;
import com.atdit.booking.financialdata.FinancialInformation;
import com.atdit.booking.financialdata.IncomeProof;
import com.atdit.booking.financialdata.LiquidAsset;
import com.atdit.booking.financialdata.SchufaOverview;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Arrays;
import java.util.Base64;


public class CustomerDatabase {

    private static final String DB_URL = "jdbc:sqlite:database/customers.db";
    private Connection connection;
    private  Customer currentCustomer;


    public CustomerDatabase(Customer customer) {
        initializeDatabase();
        this.currentCustomer = customer;
    }


    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
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

        throw new SQLException("Failed to get income proof ID");

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

        throw new SQLException("Failed to get assets proof ID");

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
        throw new SQLException("Failed to get schufa ID");
    }


    private static class Encryption {
        private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
        private static final int KEY_SIZE = 256;

        private static SecretKey deriveKey(String email, String password) throws Exception {
            String salt = "1. FC Kaiserslautern";
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, KEY_SIZE);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        }

        public static String encrypt(String value, String email, String password) throws Exception {
            SecretKey key = deriveKey(email, password);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] encrypted = cipher.doFinal(value.getBytes());
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
            return Base64.getEncoder().encodeToString(combined);
        }

        public static String decrypt(String encrypted, String email, String password) throws Exception {
            SecretKey key = deriveKey(email, password);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] decoded = Base64.getDecoder().decode(encrypted);
            byte[] iv = Arrays.copyOfRange(decoded, 0, 16);
            byte[] data = Arrays.copyOfRange(decoded, 16, decoded.length);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return new String(cipher.doFinal(data));
        }
    }

    private void insertCustomerData(long financialInfoId, String password) throws SQLException {

        String email = this.currentCustomer.getEmail();

        String sql = """
    INSERT INTO customers (email_hashed, email, title, firstName, name, country, birthdate, address, financial_info_id)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


            pstmt.setString(1, hashString(email));
            pstmt.setString(2, Encryption.encrypt(email, email, password));
            pstmt.setString(3, Encryption.encrypt(this.currentCustomer.getTitle(), email, password));
            pstmt.setString(4, Encryption.encrypt(this.currentCustomer.getFirstName(), email, password));
            pstmt.setString(5, Encryption.encrypt(this.currentCustomer.getName(), email, password));
            pstmt.setString(6, Encryption.encrypt(this.currentCustomer.getCountry(), email, password));
            pstmt.setString(7, Encryption.encrypt(this.currentCustomer.getBirthdate(), email, password));
            pstmt.setString(8, Encryption.encrypt(this.currentCustomer.getAddress(), email, password));
            pstmt.setLong(9, financialInfoId);
            pstmt.executeUpdate();


        } catch (Exception e) {
            throw new SQLException("Failed to encrypt and save customer data", e);
        }
    }

    public Customer getCustomerByEmail(String email, String password) throws SQLException, NoSuchAlgorithmException {


        String sql = "SELECT * FROM customers WHERE email_hashed = ?";


        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, hashString(email));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer();
                try {
                    customer.setEmail(Encryption.decrypt(rs.getString("email"), email, password));
                    customer.setTitle(Encryption.decrypt(rs.getString("title"), email, password));
                    customer.setFirstName(Encryption.decrypt(rs.getString("firstName"), email, password));
                    customer.setName(Encryption.decrypt(rs.getString("name"), email, password));
                    customer.setCountry(Encryption.decrypt(rs.getString("country"), email, password));
                    customer.setBirthdate(Encryption.decrypt(rs.getString("birthdate"), email, password));
                    customer.setAddress(Encryption.decrypt(rs.getString("address"), email, password));
                    return customer;

                } catch (Exception e) {
                    throw new SQLException("Failed to decrypt customer data", e);
                }
            }
            return null;
        }
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

        System.out.println("Failed to insert financial data");
        throw new SQLException("Failed to get customer ID");
    }


    public void saveCustomerInDatabase(String password) {

        try {

            long incomeProofId = insertIncomeProof();
            long assetsProofId = insertAssetsProof();
            long schufaId = insertSchufaRecord();
            long financialInfoId = insertFinancialData(incomeProofId, assetsProofId, schufaId);

            System.out.println("Financial Info ID: " + financialInfoId);

            insertCustomerData(financialInfoId, password);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save customer data:", e);
        } finally {
            closeConnection();
        }
    }

    public Customer getCustomerWithFinancialInfoByEmail(String email, String password) throws SQLException, NoSuchAlgorithmException {
        String sql = "SELECT c.*, fi.*, ip.*, la.*, so.* " +
                "FROM customers c " +
                "JOIN financial_information fi ON c.financial_info_id = fi.id " +
                "LEFT JOIN income_proofs ip ON fi.proof_of_income_id = ip.id " +
                "LEFT JOIN liquid_assets la ON fi.proof_of_liquid_assets_id = la.id " +
                "LEFT JOIN schufa_overview so ON fi.schufa_id = so.id " +
                "WHERE c.email_hashed = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, hashString(email));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                System.out.println("success");

                Customer customer = extractCustomerFromResultSet(rs, email, password);
                FinancialInformation financialInfo = extractFinancialInfoFromResultSet(rs);
                IncomeProof incomeProof = extractIncomeProofFromResultSet(rs);
                LiquidAsset liquidAsset = extractLiquidAssetFromResultSet(rs);
                SchufaOverview schufaOverview = extractSchufaOverviewFromResultSet(rs);

                financialInfo.setProofOfIncome(incomeProof);
                financialInfo.setProofOfLiquidAssets(liquidAsset);
                financialInfo.setSchufa(schufaOverview);
                customer.setFinancialInformation(financialInfo);

                return customer;
            }
            return null;
        }
    }

    private Customer extractCustomerFromResultSet(ResultSet rs, String email, String password) throws SQLException {
        Customer customer = new Customer();
        customer.setEmail(email);
        try {
            customer.setTitle(Encryption.decrypt(rs.getString("title"), email, password));
            customer.setFirstName(Encryption.decrypt(rs.getString("firstName"), email, password));
            customer.setName(Encryption.decrypt(rs.getString("name"), email, password));
            customer.setCountry(Encryption.decrypt(rs.getString("country"), email, password));
            customer.setBirthdate(Encryption.decrypt(rs.getString("birthdate"), email, password));
            customer.setAddress(Encryption.decrypt(rs.getString("address"), email, password));
            return customer;
        } catch (Exception e) {
            throw new SQLException("Failed to decrypt customer data", e);
        }
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
        int incomeProofId = rs.getInt("proof_of_income_id");
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
        int liquidAssetsId = rs.getInt("proof_of_liquid_assets_id");
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
        int schufaId = rs.getInt("schufa_id");
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
        )"""
        };

        try (Statement stmt = connection.createStatement()) {
            for (String sql : tables) {
                stmt.execute(sql);
            }
        }
    }


    public String hashString(String stringToHash) throws NoSuchAlgorithmException {

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(stringToHash.getBytes());
        byte[] someByteArray = messageDigest.digest();
        return new BigInteger(1, someByteArray).toString(16);
    }
}