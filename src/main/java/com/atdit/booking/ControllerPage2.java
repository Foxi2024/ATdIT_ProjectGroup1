package com.atdit.booking;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class ControllerPage2 extends Controller{

    @FXML
    private RadioButton acceptButton;

    @FXML
    private Button backButton;

    @FXML
    private Button continueButton;


    @FXML
    public void nextPage(MouseEvent e){

        if(acceptButton.isSelected()){
            Stage stage = (Stage) continueButton.getScene().getWindow();
            Scene scene = getScene("page_3.fxml");
            stage.setTitle("Personal Information");
            stage.setScene(scene);
        }
    }

    public void previousPage(MouseEvent e){

        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = getScene("page_1.fxml");
        stage.setTitle("Main");
        stage.setScene(scene);

    }

    @FXML
    public void acceptAgreement(MouseEvent e) {

        if(acceptButton.isSelected()){
            continueButton.setDisable(false);
        } else {
            continueButton.setDisable(true);
        }
    }

}
