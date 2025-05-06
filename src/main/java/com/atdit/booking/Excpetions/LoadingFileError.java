package com.atdit.booking.Excpetions;

public class LoadingFileError {
    static public void showLoadingFileError(String errorMessage) {
        StandardException.showError("File Error", "Error with file content", errorMessage);
    }
}
