package com.skdwa.tcp;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.net.SocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class TCPConnection implements Connection {
    @Getter
    private boolean isConnected = false;
    @Getter
    private String host;
    @Getter
    private int port;

    private SocketFactory factory;
    private Socket socket;
    private OutputStream socketOutputStream;
    private OutputStream appOutputStream;

    private ExecutorService executorService;

    public TCPConnection(String host, int port, OutputStream outputStream) {
        factory = SocketFactory.getDefault();
        this.port = port;
        this.host = host;
        executorService = Executors.newSingleThreadExecutor();
        this.appOutputStream = outputStream;
    }


    @Override
    public void connect() throws IOException {
        if (isConnected) {
            disconnect();
            isConnected = false;
        }
        this.socket = factory.createSocket(host, port);
        isConnected = true;
        System.out.println("Connected");
        startReading();
    }

    @Override
    public void disconnect() {
        try {
            if (socketOutputStream != null) {
                socketOutputStream.flush();
                socketOutputStream.close();
                socketOutputStream = null;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void startReading() {
        Thread thread = new Thread(() -> {
            while (!socket.isClosed()) {
                try (InputStreamReader ir = new InputStreamReader(socket.getInputStream());
                     BufferedReader br = new BufferedReader(ir)) {
                    String message = "";
                    while ((message = br.readLine()) != null && !message.isEmpty()) {
                        appOutputStream.write(message.getBytes());
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        });
        thread.start();
    }

    @Override
    public void write(String message) {
        if (socketOutputStream == null) {
            try {
                socketOutputStream = socket.getOutputStream();
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
        }
        executorService.submit(() -> {
            try {
                socketOutputStream.write(message.getBytes(StandardCharsets.UTF_8));
                socketOutputStream.flush();
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
        });

    }


}
