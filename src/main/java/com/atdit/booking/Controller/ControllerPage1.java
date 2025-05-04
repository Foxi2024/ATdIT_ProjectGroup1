package com.atdit.booking.Controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class ControllerPage1 extends Controller {

    @FXML private Button start_process_button;

    public void start_payment_process(javafx.scene.input.MouseEvent mouseEvent) {

        Stage stage = (Stage) start_process_button.getScene().getWindow();
        Scene scene = getScene("page_2.fxml");
        stage.setTitle("Datenschutzerklärung");
        stage.setScene(scene);
    }

}
