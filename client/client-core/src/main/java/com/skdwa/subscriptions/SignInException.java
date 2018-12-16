package com.skdwa.subscriptions;

public class SignInException extends Exception {
    public SignInException() {
        super();
    }

    public SignInException(String message) {
        super(message);
    }

    public SignInException(Throwable cause) {
        super(cause);
    }
}
