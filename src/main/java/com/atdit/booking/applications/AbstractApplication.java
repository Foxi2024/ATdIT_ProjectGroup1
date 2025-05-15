package com.atdit.booking.applications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Abstract base class for JavaFX applications in the booking system
 * This class provides common setup for loading FXML files, setting icons, and displaying the main stage
 * Subclasses must provide the specific FXML filename and window title
 */
public abstract class AbstractApplication extends Application {

    /**
     * The name of the FXML file (without path) to be loaded for the application's main scene
     * This should be set by a subclass before `launch()` is called
     */
    protected static String filename;
    /**
     * The title for the application window
     * This should be set by a subclass before `launch()` is called
     */
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