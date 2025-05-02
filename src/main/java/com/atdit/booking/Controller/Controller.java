package com.atdit.booking.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public abstract class Controller {

    public static Scene getScene(String fxml) {

        FXMLLoader loader = new FXMLLoader(Controller.class.getResource("/com/atdit/booking/" + fxml));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scene;
    }
}
