package com.atdit.booking;

import com.atdit.booking.customer.Customer;
import com.atdit.booking.customer.CustomerDatabase;
import com.atdit.booking.financialdata.*;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;



public class Main {

    public static Customer customer;
    public static FinancialInformation financialInformation;

    public static final int MIN_MONTHLY_MONEY = 1;

    public static void main(String[] args) {

        customer = new Customer();

        MainWindow.main(null);
    }
}
