package com.skdwa.subscriptions;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ListOutputStream extends OutputStream {
    private final List<String> list;
    private StringBuilder stringBuilder = new StringBuilder();

    public ListOutputStream(List<String> list) {
        super();
        this.list = list;
    }

    @Override
    public void write(int b) throws IOException {
        char c = (char) b;
        if (c == '\n') {
            list.add(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        } else {
            stringBuilder.append(c);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        String message = String.valueOf(b);
        String[] messages = message.split("\\r?\\n");
        for (String mess : messages) {
            stringBuilder.append(mess);
            list.add(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
    }
}
