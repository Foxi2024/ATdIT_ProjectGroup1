package com.atdit.booking.customer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CustomerDatabase {

    private static final String JDBC_URL = "jdbc:sqlite:database/database.db";

    public static Connection getConn() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void executeUpdate(Connection conn, String query) {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Query executed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
