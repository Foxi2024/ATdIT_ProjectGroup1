package com.atdit.booking.frontend.Controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

/**
 * The Navigatable interface defines methods for handling page navigation in the application.
 * Classes that implement this interface can manage navigation between different pages or views.
 *
 * This interface is designed to work with JavaFX FXML controllers and provides basic
 * navigation functionality through mouse events.
 */
public interface Navigatable {

    /**
     * Navigates to the next page when triggered by a mouse event.
     *
     * @param e The MouseEvent that triggered the navigation
     */
    @FXML void nextPage(MouseEvent e);

    /**
     * Navigates to the previous page when triggered by a mouse event.
     *
     * @param e The MouseEvent that triggered the navigation
     */
    @FXML void previousPage(MouseEvent e);

}