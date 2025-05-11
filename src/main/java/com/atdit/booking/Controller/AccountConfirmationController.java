package com.atdit.booking.Controller;

import com.atdit.booking.Main;
import com.atdit.booking.customer.Customer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountConfirmationController extends Controller implements Initializable {

    @FXML
    private Label emailLabel;

    public static Customer currentCustomer = Main.customer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailLabel.setText("Eine Bestätigung wird an " + currentCustomer.getEmail() + " gesendet.");
    }

    @FXML
    public void closeApplication(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}