package com.skdwa.subscriptions;

public class RegisterLoginTakenException extends Exception {
    public RegisterLoginTakenException(String message) {
        super(message);
    }
}
