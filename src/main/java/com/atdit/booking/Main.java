package com.atdit.booking;

import com.atdit.booking.backend.customer.Customer;

/**
 * Main class for the booking application.
 * This class serves as the entry point for the application and manages the customer registration process.
 */
public class Main {

    /**
     * Static customer instance used throughout the application.
     */
    public static Customer customer;

    /**
     * The main method that starts the application.
     * Initializes a new customer instance and starts the customer registration process.
     *
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {

        customer = new Customer();

        //CustomerRegistrationProcess.main(args);
        PaymentProcess.main(args);
    }
}


/*





 */