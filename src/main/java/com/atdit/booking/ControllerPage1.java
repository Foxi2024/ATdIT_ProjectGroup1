package com.atdit.booking;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class ControllerPage1 extends Controller {

    @FXML
    private Button start_process_button;

    public void start_payment_process(javafx.scene.input.MouseEvent mouseEvent) {

        Stage stage = (Stage) start_process_button.getScene().getWindow();
        Scene scene = getScene("page_2.fxml");

        stage.setTitle("Datenschutzerklärung");
        stage.setScene(scene);
    }

}
