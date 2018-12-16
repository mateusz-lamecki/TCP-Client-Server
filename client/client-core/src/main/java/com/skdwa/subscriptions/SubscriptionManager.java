package com.skdwa.subscriptions;

import com.skdwa.tcp.Connection;
import com.skdwa.tcp.TCPConnection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class SubscriptionManager {
    private final List<String> messages = new LinkedList<>();
    @Getter
    private LoggedUser loggedUser;
    @Getter
    private Connection connection;
    private OutputStream outputStream = new ListOutputStream(messages);

    /**
     * Registers user to service
     *
     * @param login    User new login
     * @param password user new password
     * @return true if register successful, false otherwise
     * @throws IOException when connection to server occurs
     */
    public boolean signUp(String login, String password) throws IOException, RegisterLoginTakenException {
        if (connection == null) {
            throw new IllegalStateException("Connection is not setted up");
        }
        if (!connection.isConnected()) {
            connection.connect();
            log.debug("{} connected to {}:{}", login, connection.getHost(), connection.getPort());
        }
        connection.write("REGISTER@@@" + login + "@@@" + password);
        byte i = 0;
        while (i++ < 10) {
            for (int l = 0; l < messages.size(); l++) {
                String message = messages.get(l);
                if (message.contains("OK")) {
                    log.info("{} registered successful!", login);
                    messages.remove(l);
                    return true;
                }
                if (message.contains("LOGIN_TAKEN")) {
                    messages.remove(l);
                    throw new RegisterLoginTakenException(login + " is already taken");
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        log.info("{} register failed, did not receive response from the server", login);
        return false;
    }

    public boolean signIn(String login, String password) throws IOException, SignInException {
        if (connection == null) {
            throw new IllegalStateException("Connection is not setted up");
        }
        if (!connection.isConnected()) {
            connection.connect();
            log.debug("{} connected to {}:{}", login, connection.getHost(), connection.getPort());
        }
        connection.write("LOGIN@@@" + login + "@@@" + password);
        byte i = 0;
        while (i++ < 10) {
            for (int l = 0; l < messages.size(); l++) {
                String message = messages.get(l);
                if (message.contains("OK")) {
                    String[] splitted = message.split("@@@");
                    if (splitted.length != 2) {
                        throw new IllegalStateException("Cannot find token in server response");
                    }
                    String token = splitted[1];
                    loggedUser = new LoggedUser(login, token);
                    log.info("{} login successful - token: {}", login, token);
                    messages.remove(l);
                    return true;
                }
                if (message.contains("INVALID_PASSWORD")) {
                    messages.remove(l);
                    throw new SignInException("Login or password is incorrect");
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        log.info("{} login failed, did not receive response from the server", login);
        return false;
    }

    public void setConnection(String host, int port) {
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
            log.info("Disconnected");
        }
        this.connection = new TCPConnection(host, port, outputStream);
    }
}
