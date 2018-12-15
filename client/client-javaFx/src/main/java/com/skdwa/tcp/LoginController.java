package com.skdwa.tcp;

import com.skdwa.subscriptions.RegisterLoginTakenException;
import com.skdwa.subscriptions.SubscriptionManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

@Slf4j
public class LoginController {
    private SubscriptionManager subscriptionManager;
    private String host;
    private int port;
    private String login;
    private String password;

    @FXML
    private TextField hostFX;
    @FXML
    private TextField portFX;
    @FXML
    private TextField loginFX;
    @FXML
    private TextField passwordFX;

    @FXML
    private Label hostError;
    @FXML
    private Label portError;
    @FXML
    private Label loginError;
    @FXML
    private Label passwordError;

    public LoginController(SubscriptionManager subscriptionManager) {
        this.subscriptionManager = subscriptionManager;
    }

    @FXML
    private void login(Event event) {
//        clearErrors();
//        String host = hostFX.getText();
//        String port = portFX.getText();
//        String login = loginFX.getText();
//        String password = passwordFX.getText();
//
//        tcpConnection = new TCPConnection(host, Integer.valueOf(port));
//        try {
//            tcpConnection.connect();
//            tcpConnection.write("LOGIN@@@" + login + "@@@" + password);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @FXML
    private void register(Event event) {
        try {
            if (validateFields()) {
                connect();
            }
            try {
                subscriptionManager.signUp(login, password);
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
            } catch (RegisterLoginTakenException e) {
                loginError.setText("\'" + login + "\' is already taken");
                loginError.setVisible(true);
            }
        } catch (InvalidFieldValueException e) {
            e.printStackTrace();
        }

    }

    private boolean validateFields() throws InvalidFieldValueException {
        clearErrors();
        return readFXFields();
    }

    private void connect() {
        subscriptionManager.setConnection(host, port);
    }

    private boolean readFXFields() throws InvalidFieldValueException {
        boolean isValid = true;
        try {
            this.host = hostFX.getText();
            if (StringUtils.isNumeric(portFX.getText())) {
                this.port = Integer.parseInt(portFX.getText());
            } else {
                portError.setText("Only digits are allowed");
                portError.setVisible(true);
                isValid = false;
            }
            if (loginFX.getText().contains("@@@")) {
                loginError.setText("Login cannot contains @@@");
                loginError.setVisible(true);
                isValid = false;
            } else {
                this.login = loginFX.getText();
            }
            if (passwordFX.getText().contains("@@@")) {
                passwordError.setText("Login cannot contains @@@");
                passwordError.setVisible(true);
                isValid = false;
            } else {
                this.password = passwordFX.getText();
            }
        } catch (NumberFormatException e) {
            throw new InvalidFieldValueException(e);
        }
        return isValid;
    }

    private void clearErrors() {
        hostError.setVisible(false);
        portError.setVisible(false);
        loginError.setVisible(false);
        passwordError.setVisible(false);
    }

}