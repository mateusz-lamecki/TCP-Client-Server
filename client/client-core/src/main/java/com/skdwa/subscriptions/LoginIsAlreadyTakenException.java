package com.skdwa.subscriptions;

public class LoginIsAlreadyTakenException extends Exception {
    public LoginIsAlreadyTakenException(String message) {
        super(message);
    }
}
