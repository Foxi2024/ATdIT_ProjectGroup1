package com.atdit.booking;

import com.atdit.booking.backend.customer.Customer;

public class Main {

    public static Customer customer;

    public static final int MIN_MONTHLY_MONEY = 1;

    public static void main(String[] args) {

        customer = new Customer();

        CustomerRegistrationProcess.main(args);
    }
}


/*





 */