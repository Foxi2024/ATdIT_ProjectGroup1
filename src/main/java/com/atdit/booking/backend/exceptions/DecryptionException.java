package com.atdit.booking.backend.exceptions;

public class DecryptionException extends CryptographyException {
  public DecryptionException(String message) {
    super(message);
  }
}
