package com.atdit.booking.Controller;

import com.atdit.booking.Main;
import com.atdit.booking.customer.Customer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPage6 extends Controller implements Initializable {

    @FXML
    private Label emailLabel;

    private static final Customer currentCustomer = Main.customer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailLabel.setText(currentCustomer.getEmail());
    }
}