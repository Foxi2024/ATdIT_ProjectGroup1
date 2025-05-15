package com.atdit.booking.applications;

/**
 * Main application class for the payment process
 * This class extends {@link AbstractApplication} to launch the payment process UI
 */
public class PaymentProcess extends AbstractApplication {

    /**
     * The main entry point for the payment process application
     * Sets the FXML filename and title for the initial screen (customer login) and launches the JavaFX application
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        filename = "payment_process/page_1_customer_login.fxml";
        title = "Anmeldung";
        launch();
    }
}