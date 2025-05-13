package com.atdit.booking.backend.exceptions;

/**
 * Custom exception class for handling encryption-related errors.
 * This class extends {@link CryptographyException} to provide specific exception handling
 * for encryption operations in the application.
 */
public class EncryptionException extends CryptographyException {

  /**
   * Constructs a new EncryptionException with the specified detail message.
   *
   * @param message the detail message that describes the error/exception
   */
  public EncryptionException(String message) {
    super(message);
  }
}