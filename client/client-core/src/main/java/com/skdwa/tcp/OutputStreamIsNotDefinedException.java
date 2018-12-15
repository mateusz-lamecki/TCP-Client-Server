package com.skdwa.tcp;

public class OutputStreamIsNotDefinedException extends Exception {
    public OutputStreamIsNotDefinedException() {
    }

    public OutputStreamIsNotDefinedException(String message) {
        super(message);
    }

    public OutputStreamIsNotDefinedException(Throwable cause) {
        super(cause);
    }
}
