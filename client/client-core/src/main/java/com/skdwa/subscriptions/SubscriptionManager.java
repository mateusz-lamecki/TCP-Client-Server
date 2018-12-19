package com.skdwa.subscriptions;

import com.google.common.base.Strings;
import com.skdwa.tcp.Connection;
import com.skdwa.tcp.TCPConnection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
        connectIfNotConnected();
        connection.write("REGISTER@@@" + login + "@@@" + password);
        Response response = waitForResponse("OK", "LOGIN_TAKEN", 2);
        if (response == null) {
            log.info("{} login failed, did not receive response from the server", login);
            return false;
        }
        if (!response.isOk()) {
            throw new RegisterLoginTakenException("login" + " is taken");
        } else {
            loggedUser = new LoggedUser(login, response.getMessage());
            return true;
        }
    }

    public boolean signIn(String login, String password) throws IOException, SignInException {
        connectIfNotConnected();
        connection.write("LOGIN@@@" + login + "@@@" + password);
        byte i = 0;
        Response response = waitForResponse("OK", "INVALID_PASSWORD", 2);
        if (response == null) {
            log.info("{} login failed, did not receive response from the server", login);
            return false;
        }
        if (!response.isOk()) {
            throw new SignInException("Login or password is incorrect");
        } else {
            loggedUser = new LoggedUser(login, response.getMessage());
            return true;
        }
    }

    public List<String> getUserSubscriptions() throws IOException, ResponseException {
        if (!isUserLogged()) {
            throw new IllegalStateException("User is not logged");
        }
        connectIfNotConnected();
        connection.write("READ_TOPICS@@@" + loggedUser.getToken());
        Response response = waitForResponse("OK", "INVALID_TOKEN", -1);
        if (response == null) {
            log.info("Did not receive response from the server");
            return null;
        }
        if (!response.isOk()) {
            throw new ResponseException("Invalid token");
        } else {
            String[] subjects = response.getMessage().split("\\$\\$\\$");
            return new ArrayList<>(Arrays.asList(subjects));
        }
    }

    public void setConnection(String host, int port) {
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
            log.info("Disconnected");
        }
        this.connection = new TCPConnection(host, port, outputStream);
    }

    private Response waitForResponse(String okMessage, String wrongMessage, int numberOfMessages) {
        int i = 0;
        while (i++ < 10) {
            for (int l = 0; l < messages.size(); l++) {
                String message = messages.get(l);
                if (message.contains(okMessage)) {
                    String[] splitted = message.split("@@@");
                    if (numberOfMessages > 0 && splitted.length != numberOfMessages) {
                        throw new IllegalStateException("Cannot find token in server response");
                    }
                    String returnValue;
                    if (splitted.length == 1) {
                        returnValue = "";
                    } else {
                        returnValue = splitted[1];
                    }
                    messages.remove(l);
                    return new Response(true, returnValue);
                }
                if (message.contains(wrongMessage)) {
                    messages.remove(l);
                    return new Response(false);
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    private void connectIfNotConnected() throws IOException {
        if (connection == null) {
            throw new IllegalStateException("Connection is not setted up");
        }
        if (!connection.isConnected()) {
            connection.connect();
            log.debug("Connected to {}:{}", connection.getHost(), connection.getPort());
        }
    }

    private boolean isUserLogged() {
        return loggedUser != null && !Strings.isNullOrEmpty(loggedUser.getToken()) && !Strings.isNullOrEmpty(loggedUser.getUsername());
    }
}
