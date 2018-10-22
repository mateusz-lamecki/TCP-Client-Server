package com.skdwa.tcp;

import javax.net.SocketFactory;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TCPConnection implements Connection {
    private boolean isConnected = false;
    private String host;
    private int port;


    private SocketFactory factory;
    private Socket socket;

    private ExecutorService executorService;

    public TCPConnection(String host, int port) {
        factory = SocketFactory.getDefault();
        this.port = port;
        this.host = host;
        executorService = Executors.newSingleThreadExecutor();
    }


    @Override
    public void connect() throws IOException {
        this.socket = factory.createSocket(host, port);
        System.out.println("Connected");
    }

    @Override
    public void read(OutputStream outputStream) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(outputStream);
             InputStream is = socket.getInputStream()) {

            String clientAddress = socket.getInetAddress().getHostAddress();
            System.out.println("\r\nNew connection from " + clientAddress);

            byte[] contents = new byte[10000];

            //No of bytes read in one read() call
            int bytesRead = 0;

            while ((bytesRead = is.read(contents)) != -1)
                bos.write(contents, 0, bytesRead);
            bos.flush();
            System.out.println("File saved successfully!");
        } catch (SocketException e) {
            System.out.println("Koniec połączenia");
        }
    }

    @Override
    public void write(String message) {
        executorService.submit(() -> {
            try (PrintWriter output = new PrintWriter(socket.getOutputStream())) {
                output.print(message);
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


}
