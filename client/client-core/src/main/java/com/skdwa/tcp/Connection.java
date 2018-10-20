package com.skdwa.tcp;

import java.io.IOException;

public interface Connection {
    void connect() throws IOException;

    void read() throws IOException;

    void write(String message) throws IOException;
}
