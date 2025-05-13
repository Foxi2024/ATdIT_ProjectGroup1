package com.atdit.booking.backend.exceptions;

/**
 * Custom exception class for handling hashing-related errors.
 * This class extends {@link CryptographyException} to specifically handle
 * exceptions that occur during hashing operations.
 */
public class HashingException extends CryptographyException {

  /**
   * Constructs a new HashingException with the specified detail message.
   *
   * @param message The detail message that provides information about the cause of the exception.
   *               This message can be retrieved later by the {@link Throwable#getMessage()} method.
   */
  public HashingException(String message) {
    super(message);
  }
}