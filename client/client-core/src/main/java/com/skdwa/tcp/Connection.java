package com.skdwa.tcp;

import java.io.IOException;
import java.io.OutputStream;

public interface Connection {
    void connect() throws IOException;

    void read(OutputStream outputStream) throws IOException;

    void write(String message) throws IOException;
}
