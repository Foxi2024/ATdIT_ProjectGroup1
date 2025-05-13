package com.atdit.booking.backend.exceptions;

/**
 * Custom exception class for handling decryption-related errors.
 * This exception is thrown when a decryption operation fails.
 * It extends the {@link CryptographyException} class to maintain
 * a hierarchical exception structure for cryptographic operations.
 */
public class DecryptionException extends CryptographyException {

  /**
   * Constructs a new DecryptionException with the specified error message.
   *
   * @param message The detailed message describing the decryption error.
   *               This message can be retrieved later by the getMessage() method.
   */
  public DecryptionException(String message) {
    super(message);
  }
}
