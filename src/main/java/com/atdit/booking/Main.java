package com.atdit.booking;

import com.atdit.booking.customer.Customer;
import com.atdit.booking.financialdata.*;

import java.net.MalformedURLException;
import java.util.ArrayList;



public class Main {

    public static Customer customer;
    public static FinancialInformation financialInformation;

    public static final int MIN_MONTHLY_MONEY = 1;

    public static void main(String[] args) {

        customer = new Customer();

        FinancialDocumentsGenerator.main(null);
        //MainWindow.main(null);
    }
}
