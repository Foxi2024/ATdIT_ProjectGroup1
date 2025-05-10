package com.atdit.booking;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public interface Navigatable {

    @FXML
    void nextPage(MouseEvent e);

    @FXML
    void previousPage(MouseEvent e);


}
