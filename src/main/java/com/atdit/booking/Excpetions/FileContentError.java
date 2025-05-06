package com.atdit.booking.Excpetions;

public class FileContentError {
    static public void showFileContentError(String errorMessage) {
        StandardException.showError("File Error", "Error with file content", errorMessage);
    }
}
