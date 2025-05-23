package com.atdit.booking.frontend.abstract_controller;


import com.atdit.booking.applications.CustomerRegistration;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Abstract base controller class for handling JavaFX scene management and error display.
 * This class provides common functionality for all controllers in the application.
 */
public abstract class Controller {

    /**
     * Loads and displays a new JavaFX scene.
     * This method handles the complete scene transition process including:
     * - Loading the FXML file
     * - Setting up the stage
     * - Positioning the window at the center of the screen
     * - Adding the application icon
     *
     * @param event The event that triggered the scene change
     * @param fxml  The name of the FXML file to load (without path)
     * @param title The title to be displayed in the window title bar
     */
    public void loadScene(Event event, String fxml, String title) {

        try {
            Stage stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(Controller.class.getResource("/com/atdit/booking/fxml_files/" + fxml));
            Scene scene = new Scene(loader.load());
            stage.setTitle(title);
            stage.setScene(scene);
            stage.getIcons().add(new Image(CustomerRegistration.class.getResourceAsStream("/com/atdit/booking/style/images/icon.png")));

            stage.sizeToScene();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);

            stage.show();
        } catch (IOException e) {
            showError("Error", "Error loading page", "Could not load the next page. Try again or close the application. \n" + e.getMessage());
        }
    }

    /**
     * Displays an error dialog with the specified information.
     * Creates and shows a JavaFX Alert dialog of type ERROR.
     *
     * @param title   The title of the error dialog window
     * @param header  The header text shown in the error dialog
     * @param content The detailed error message to be displayed
     */
    public void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation dialog with options to correct information or cancel the process.
     * If the user chooses to cancel, the application will exit.
     *
     * @param title   The title of the confirmation dialog window
     * @param header  The header text shown in the confirmation dialog
     * @param content The detailed message or question to be displayed
     */
    public void showConfirmation(String title, String header, String content) {
        ButtonType correctButton = new ButtonType("Korrigieren");
        ButtonType cancelButton = new ButtonType("Prozess abbrechen");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getButtonTypes().setAll(correctButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == cancelButton) {
                Platform.exit();
            }

        });
    }


}