package com.atdit.booking.customer;

public class HashingException extends Exception {
    public HashingException(String message) {
        super(message);
    }

    public HashingException(String message, Throwable cause) {
        super(message, cause);
    }
}