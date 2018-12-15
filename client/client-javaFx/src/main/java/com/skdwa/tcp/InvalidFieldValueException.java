package com.skdwa.tcp;

public class InvalidFieldValueException extends Exception {
    public InvalidFieldValueException() {
    }

    public InvalidFieldValueException(String message) {
        super(message);
    }

    public InvalidFieldValueException(Throwable cause) {
        super(cause);
    }
}
