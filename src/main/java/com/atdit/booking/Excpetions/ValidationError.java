package com.atdit.booking.Excpetions;

public class ValidationError {
    static public void showValidationError(String errorMessage) {
        StandardException.showError("Validation Error", "Validation Error in your declarations", errorMessage);
    }
}
