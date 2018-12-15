package com.skdwa.subscriptions;

public class SubscriptionException extends Exception {
    public SubscriptionException() {
    }

    public SubscriptionException(String message) {
        super(message);
    }

    public SubscriptionException(Throwable cause) {
        super(cause);
    }
}
