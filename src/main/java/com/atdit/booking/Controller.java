package com.atdit.booking;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class Controller {

    public static Scene getScene(String fxml) {

        FXMLLoader loader = new FXMLLoader(Controller.class.getResource(fxml));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scene;
    }
}
