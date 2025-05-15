package com.atdit.booking.applications;

/**
 * Main application class for the customer registration process
 * This class extends {@link AbstractApplication} to launch the customer registration UI
 */
public class CustomerRegistration extends AbstractApplication {

    /**
     * The main entry point for the customer registration application
     * Sets the FXML filename and title for the initial screen and launches the JavaFX application
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        filename = "customer_registration/page_1_start_customer_registration.fxml";
        title = "Start";
        launch();
    }
}