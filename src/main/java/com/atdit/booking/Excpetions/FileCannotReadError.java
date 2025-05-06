package com.atdit.booking.Excpetions;

public class FileCannotReadError {
    static public void showFileCannotReadError(String errorMessage) {
        StandardException.showError("File Error", "Cannot read file", errorMessage);
    }
}
