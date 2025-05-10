package com.atdit.booking.Controller;

import javafx.scene.input.MouseEvent;

public class Page1PaymentProcessStartController extends Controller {

    public void start_payment_process(MouseEvent e) {

        loadScene(e,"page_2.fxml", "Data Privacy Agreement");
    }

}
