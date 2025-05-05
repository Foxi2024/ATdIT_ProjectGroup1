package com.atdit.booking.customer;

import com.atdit.booking.Main;

import java.sql.*;

public class CustomerDatabase {

    private static final String DB_URL = "jdbc:sqlite:database/customers.db";
    private Connection connection;
    private  Customer currentCustomer;

    public CustomerDatabase(Customer customer) {
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

    private void createTables() throws SQLException {
        String[] tables = {
                """
        CREATE TABLE IF NOT EXISTS customers (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            email TEXT UNIQUE,
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

    private long insertIncomeProof() throws SQLException {


        String sql = """
        INSERT INTO income_proofs (monthlyNetIncome, employer, employmentType, 
        employmentDurationMonths, dateIssued)
        VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var proof = this.currentCustomer.getFinancialInformation().getProofOfIncome();
            pstmt.setInt(1, proof.monthlyNetIncome());
            pstmt.setString(2, proof.employer());
            pstmt.setString(3, proof.employmentType());
            pstmt.setInt(4, proof.employmentDurationMonths());
            pstmt.setString(5, proof.dateIssued());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) return rs.getLong(1);
            throw new SQLException("Failed to get income proof ID");
        }
    }

    private long insertAssetsProof(Customer customer) throws SQLException {
        String sql = """
        INSERT INTO liquid_assets (iban, description, balance, dateIssued)
        VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var proof = customer.getFinancialInformation().getProofOfLiquidAssets();
            pstmt.setString(1, proof.iban());
            pstmt.setString(2, proof.description());
            pstmt.setInt(3, proof.balance());
            pstmt.setString(4, proof.dateIssued());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) return rs.getLong(1);
            throw new SQLException("Failed to get assets proof ID");
        }
    }

    private long insertSchufaRecord(Customer customer) throws SQLException {
        String sql = """
        INSERT INTO schufa_overview (firstName, lastName, score, totalCredits,
        totalCreditSum, totalAmountPayed, totalAmountOwed, totalMonthlyRate, dateIssued)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var schufa = customer.getFinancialInformation().getSchufa();
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
            if (rs.next()) return rs.getLong(1);
            throw new SQLException("Failed to get schufa ID");
        }
    }

    private long insertCustomerData(Customer customer) throws SQLException {
        String sql = """
        INSERT INTO customers (email, title, firstName, name, country, birthdate, address)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, customer.getEmail());
            pstmt.setString(2, customer.getTitle());
            pstmt.setString(3, customer.getFirstName());
            pstmt.setString(4, customer.getName());
            pstmt.setString(5, customer.getCountry());
            pstmt.setString(6, customer.getBirthdate());
            pstmt.setString(7, customer.getAddress());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) return rs.getLong(1);
            throw new SQLException("Failed to get customer ID");
        }
    }

    private void insertFinancialData(long customerId, Customer customer, long incomeProofId,
                                     long assetsProofId, long schufaId) throws SQLException {
        String sql = """
        INSERT INTO financial_information
        (avgNetIncome, liquidAssets, monthlyFixCost, minCostOfLiving, 
        monthlyAvailableMoney, proof_of_income_id, proof_of_liquid_assets_id, schufa_id)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            var financialInfo = customer.getFinancialInformation();
            pstmt.setInt(1, financialInfo.getAvgNetIncome());
            pstmt.setInt(2, financialInfo.getLiquidAssets());
            pstmt.setInt(3, financialInfo.getMonthlyFixCost());
            pstmt.setInt(4, financialInfo.getMinCostOfLiving());
            pstmt.setInt(5, financialInfo.getMonthlyAvailableMoney());
            pstmt.setLong(6, incomeProofId);
            pstmt.setLong(7, assetsProofId);
            pstmt.setLong(8, schufaId);
            pstmt.executeUpdate();
        }
    }

    public void saveCustomerInDatabase() {
        //initializeDatabase();

        try {
            long incomeProofId = insertIncomeProof();
            long assetsProofId = insertAssetsProof(currentCustomer);
            long schufaId = insertSchufaRecord(currentCustomer);
            long customerId = insertCustomerData(currentCustomer);
            insertFinancialData(customerId, currentCustomer, incomeProofId, assetsProofId, schufaId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save customer data", e);
        } finally {
            closeConnection();
        }
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

    public static void main(String[] args) {

    }
}