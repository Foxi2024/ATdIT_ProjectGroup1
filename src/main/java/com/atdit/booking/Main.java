package com.atdit.booking;

import com.atdit.booking.customer.Customer;
import com.atdit.booking.financialdata.FinancialInformation;

public class Main {

    public static final int MIN_MONTHLY_MONEY = 1;

    public static void main(String[] args) {


        FinancialInformation.createFinancialInformationTable();
    }
}
