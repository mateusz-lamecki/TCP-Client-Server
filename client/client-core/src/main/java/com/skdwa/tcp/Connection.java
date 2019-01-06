package com.skdwa.tcp;

import java.io.IOException;

public interface Connection {
    void connect() throws IOException;
    void disconnect();
    void write(String message) throws IOException;

    String getHost();

    int getPort();

    boolean isConnected();
}
