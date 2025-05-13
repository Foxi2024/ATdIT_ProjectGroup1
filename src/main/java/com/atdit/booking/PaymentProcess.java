package com.atdit.booking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for the customer registration process.
 * This class extends JavaFX Application to create and display the main window.
 */
public class PaymentProcess extends Application {

    /**
     * Initializes and starts the main application window.
     * This method is called by the JavaFX runtime after launch().
     *
     * @param stage The primary stage for this application
     * @throws IOException If the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CustomerRegistrationProcess.class.getResource("fxml_files/customer_login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image(CustomerRegistrationProcess.class.getResourceAsStream("style/images/icon.png")));
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main entry point for the application.
     * Launches the JavaFX runtime and the application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}