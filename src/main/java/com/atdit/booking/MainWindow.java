package com.atdit.booking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("page_1" + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        //stage.getIcons().add(new Image(MainWindow.class.getResourceAsStream("icon.png")));
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}