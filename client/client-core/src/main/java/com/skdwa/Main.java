package com.skdwa;

import com.skdwa.tcp.Connection;
import com.skdwa.tcp.TCPConnection;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection connection = new TCPConnection("192.168.0.22", 3552);
        connection.connect();
        connection.write("Witaj \t Siemaneczko \n\r cześć");
    }
}
