package com.atdit.booking.Excpetions;

public class DatabaseSavingError {
    static public void showDatabaseSavingError(String errorMessage) {
        StandardException.showError("Database Error", "There was an error while saving your data.", errorMessage);
    }
}
