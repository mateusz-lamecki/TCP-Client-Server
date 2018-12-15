package com.skdwa.subscriptions;

public class RegisterLoginTakenException extends Exception {
    public RegisterLoginTakenException() {
    }

    public RegisterLoginTakenException(String message) {
        super(message);
    }

    public RegisterLoginTakenException(Throwable cause) {
        super(cause);
    }
}
