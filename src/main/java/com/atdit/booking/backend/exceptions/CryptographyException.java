package com.atdit.booking.backend.exceptions;

/**
 * Custom runtime exception for cryptography-related errors.
 * This exception is thrown when cryptographic operations fail during execution.
 *
 */
public class CryptographyException extends RuntimeException {

    /**
     * Constructs a new CryptographyException with the specified detail message.
     *
     * @param message The detail message that provides information about the cause of the exception.
     *               This message can be retrieved later by the {@link Throwable#getMessage()} method.
     */
    public CryptographyException(String message) {
        super(message);
    }
}