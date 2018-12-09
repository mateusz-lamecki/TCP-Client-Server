package com.skdwa.tcp;

import javax.net.SocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TCPConnection implements Connection {
    private boolean isConnected = false;
    private String host;
    private int port;


    private SocketFactory factory;
    private Socket socket;
    private PrintWriter socketOutputStream = null;

    private ExecutorService executorService;

    public TCPConnection(String host, int port) {
        factory = SocketFactory.getDefault();
        this.port = port;
        this.host = host;
        executorService = Executors.newSingleThreadExecutor();
    }


    @Override
    public void connect() throws IOException {
        if (isConnected) {
            disconnect();
        }
        this.socket = factory.createSocket(host, port);
        System.out.println("Connected");
    }

    @Override
    public void disconnect() {
        if (socketOutputStream != null) {
            socketOutputStream.flush();
            socketOutputStream.close();
            socketOutputStream = null;
        }
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        isConnected = false;
    }

    @Override
    public void read(OutputStream outputStream) {
        Thread thread = new Thread(() -> {
            while (!socket.isClosed()) {
                try (InputStreamReader ir = new InputStreamReader(socket.getInputStream());
                     BufferedReader br = new BufferedReader(ir)) {
                    StringBuilder builder = new StringBuilder();
                    String message = "";
                    while ((message = br.readLine()) != null && !message.isEmpty()) {
                        outputStream.write(message.getBytes());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    @Override
    public void write(String message) {
        if (socketOutputStream == null) {
            try {
                socketOutputStream = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        executorService.submit(() -> {
            socketOutputStream.print(message);
            socketOutputStream.flush();
        });

    }


}
