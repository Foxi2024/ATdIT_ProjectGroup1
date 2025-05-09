package com.atdit.booking.Controller;

import javafx.scene.input.MouseEvent;

public class PaymentProcessStartController extends Controller {

    public void start_payment_process(MouseEvent e) {

        loadScene(e,"page_3.fxml", "Datenschutzerklärung");
    }

}
