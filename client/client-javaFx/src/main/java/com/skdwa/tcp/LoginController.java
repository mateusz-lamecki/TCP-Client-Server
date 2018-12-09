package com.skdwa.tcp;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;


public class LoginController {
    private Session session;

    public LoginController(Session session) {
        this.session = session;
    }

    @FXML
    private TextField hostFX;
    @FXML
    private TextField portFX;
    @FXML
    private TextField loginFX;
    @FXML
    private TextField passwordFX;

    private Connection tcpConnection;

    @FXML
    private void login(Event event) {
        String host = hostFX.getText();
        String port = portFX.getText();
        String login = loginFX.getText();
        String password = passwordFX.getText();

        tcpConnection = new TCPConnection(host, Integer.valueOf(port));
        try {
            tcpConnection.connect();
            tcpConnection.write("LOGIN@@@" + login + "@@@" + password);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void register(Event event) {
        String host = hostFX.getText();
        String port = portFX.getText();
        String login = loginFX.getText();
        String password = passwordFX.getText();

        tcpConnection = new TCPConnection(host, Integer.valueOf(port));
        try {
            tcpConnection.connect();
            tcpConnection.write("REGISTER@@@" + login + "@@@" + password);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}