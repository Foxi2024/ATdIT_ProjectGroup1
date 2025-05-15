package com.atdit.booking.applications;

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
public abstract class AbstractApplication extends Application {

    protected static String filename;
    protected static String title;

    /**
     * Initializes and starts the main application window.
     * This method is called by the JavaFX runtime after launch().
     *
     * @param stage The primary stage for this application
     * @throws IOException If the FXML file cannot be loaded
     */

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CustomerRegistration.class.getResource("/com/atdit/booking/fxml_files/" + filename));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image(CustomerRegistration.class.getResourceAsStream("/com/atdit/booking/style/images/icon.png")));
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}