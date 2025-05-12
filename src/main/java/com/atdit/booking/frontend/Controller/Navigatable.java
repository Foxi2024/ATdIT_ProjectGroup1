package com.atdit.booking.frontend.Controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public interface Navigatable {

    @FXML void nextPage(MouseEvent e);
    @FXML void previousPage(MouseEvent e);

}
