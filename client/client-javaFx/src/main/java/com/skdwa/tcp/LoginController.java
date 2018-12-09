package com.skdwa.tcp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {
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