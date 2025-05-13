package com.atdit.booking.backend.exceptions;

/**
 * Custom runtime exception for handling validation-related errors.
 * This exception is thrown when validation operations fail during execution.
 *
 * This class extends {@link RuntimeException} to provide specific exception handling
 * for validation operations in the application. As a runtime exception, it does not
 * need to be explicitly declared in method signatures.
 */
public class ValidationException extends RuntimeException {
    /**
     * Constructs a new ValidationException with the specified detail message.
     *
     * @param message The detail message that provides information about the cause of the exception.
     *               This message can be retrieved later by the {@link Throwable#getMessage()} method.
     */
    public ValidationException(String message) {
        super(message);
    }
}