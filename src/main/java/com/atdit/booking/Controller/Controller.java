package com.atdit.booking.Controller;


import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;



public abstract class Controller {

    public void loadScene(Event event, String fxml, String title) {

        try {
            Stage stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(Controller.class.getResource("/com/atdit/booking/" + fxml));
            Scene scene = new Scene(loader.load());
            stage.setTitle(title);
            stage.setScene(scene);

            stage.sizeToScene();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);

            stage.show();
        }
        catch (IOException e) {
            showError("Error", "Error loading page", "Could not load the next page. Try again or close the application. \n" + e.getMessage());
        }
    }

    static public void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
