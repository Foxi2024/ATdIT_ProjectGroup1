package com.atdit.booking.Excpetions;

public class InvalidInputError {
    static public void showInvalidInputError(String errorMessage) {
        StandardException.showError("Invalid Input", "Please correct the input", errorMessage);
    }
}
