package com.atdit.booking.backend.exceptions;

/**
 * Custom runtime exception for handling evaluation-related errors.
 * This exception is thrown when evaluation operations fail during execution.
 *
 * This class extends {@link RuntimeException} to provide specific exception handling
 * for evaluation operations in the application. It follows the pattern of unchecked
 * exceptions, meaning it does not need to be explicitly declared in method signatures.
 */
public class EvaluationException extends RuntimeException {
    /**
     * Constructs a new EvaluationException with the specified detail message.
     *
     * @param message The detail message that provides information about the cause of the exception.
     *               This message can be retrieved later by the {@link Throwable#getMessage()} method.
     */
    public EvaluationException(String message) {
        super(message);
    }
}