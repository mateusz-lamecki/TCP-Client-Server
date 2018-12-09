package com.skdwa;

import com.skdwa.tcp.Connection;
import com.skdwa.tcp.TCPConnection;

import java.io.OutputStream;

public class Client {
    public static void main(String[] args) throws Exception {
        Connection connection = new TCPConnection("localhost", 3552);
        connection.connect();
        OutputStream os = System.out;
        connection.read(os);
        connection.write("LOGIN@@@mati@@@password");
        //connection.write("Witaj \t Siemaneczko \n\r cześć");
    }
}
