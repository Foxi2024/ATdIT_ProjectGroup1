package com.atdit.booking.Excpetions;

public class DatabseException {
  static public void showPageLoadingError(String errorMessage) {
    StandardException.showError("Error", "Error loading page", "Could not load the next page. Try again or close the application. \n" + errorMessage);
  }
}
