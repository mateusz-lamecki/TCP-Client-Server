package com.skdwa.tcp;

import javax.net.SocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class TCPConnection implements Connection {
    private boolean isConnected = false;
    private String host;
    private int port;


    private SocketFactory factory;
    private Socket socket;

    public TCPConnection(String host, int port) {
        factory = SocketFactory.getDefault();
        this.port = port;
        this.host = host;
    }


    @Override
    public void connect() throws IOException {
        this.socket = factory.createSocket(host, port);
        System.out.println("Connected");
    }

    @Override
    public void read() throws IOException {
        try (InputStream is = socket.getInputStream()) {

        }
    }

    @Override
    public void write(String message) throws IOException {
        PrintWriter output = new PrintWriter(socket.getOutputStream());
        output.print(message);
        output.flush();
        output.close();
    }


}
