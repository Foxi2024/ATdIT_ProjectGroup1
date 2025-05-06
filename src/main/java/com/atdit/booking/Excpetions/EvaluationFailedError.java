package com.atdit.booking.Excpetions;

public class EvaluationFailedError {
    static public void showEvaluationDeclarationFailedError(String errorMessage) {
        StandardException.showError("Evaluation Error", "Evaluation Error in your declarations", errorMessage);
    }

    static public void showEvaluationFailedExtendedError() {
        StandardException.showError("Evaluation Failed", "Customer evaluation failed", "Please check your financial information.");
    }

    static public void showEvaluationPersonalInformationFailedError(String errorMessage) {
        StandardException.showError("Evaluation Failed", "Evaluation of personal Information failed", errorMessage);
    }
}
