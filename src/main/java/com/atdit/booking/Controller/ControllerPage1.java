package com.atdit.booking.Controller;

import javafx.scene.input.MouseEvent;

public class ControllerPage1 extends Controller {

    public void start_payment_process(MouseEvent e) {

        loadScene(e,"page_4.fxml", "Datenschutzerklärung");
    }

}
