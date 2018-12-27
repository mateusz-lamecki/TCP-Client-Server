package com.skdwa.tcp.signpanel;

import com.google.common.base.Strings;
import com.skdwa.subscriptions.RegisterLoginTakenException;
import com.skdwa.subscriptions.SignInException;
import com.skdwa.subscriptions.SubscriptionManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class LoginController {
    private SubscriptionManager subscriptionManager;
    private boolean urlChanged = false;
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
    @FXML
    private Label infoFx;

    public LoginController(SubscriptionManager subscriptionManager) {
        this.subscriptionManager = subscriptionManager;
    }

    @FXML
    private void login(Event event) {
        try {
            if (!validateFields()) {
                return;
            }
            if (urlChanged) {
                connect();
            }
            boolean signInSuccessful = subscriptionManager.signIn(login, password);
            if (signInSuccessful) {
                exitScene();
            } else {
                infoFx.setText("Login unsuccessful\nTry again");
                infoFx.setTextFill(Color.RED);
                infoFx.setVisible(true);
            }
        } catch (SignInException e) {
            loginError.setText("Login/Password incorrect");
            loginError.setVisible(true);
        } catch (InvalidFieldValueException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    @FXML
    private void register(Event event) {
        try {
            if (!validateFields()) {
                return;
            }
            if (urlChanged) {
                connect();
            }
            boolean signUpSuccessful = subscriptionManager.signUp(login, password);
            if (signUpSuccessful) {
                infoFx.setText("Register successful");
                infoFx.setTextFill(Color.GREEN);
            } else {
                infoFx.setText("Register unsuccessful");
                infoFx.setTextFill(Color.RED);
            }
            infoFx.setVisible(true);
        } catch (RegisterLoginTakenException e) {
            loginError.setText("\'" + login + "\' is already taken");
            loginError.setVisible(true);
        } catch (InvalidFieldValueException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    private boolean validateFields() throws InvalidFieldValueException {
        clearInfoFields();
        return readFXFields();
    }

    private void connect() {
        subscriptionManager.setConnection(host, port);
    }

    private boolean readFXFields() throws InvalidFieldValueException {
        boolean isValid = true;
        urlChanged = false;
        try {
            //HOST
            ValidationStatus lastValidation = FieldsValidator.hostValidator(hostFX.getText());
            if (lastValidation.isValid()) {
                if (Strings.isNullOrEmpty(host) || !host.equals(hostFX.getText())) {
                    host = hostFX.getText();
                    urlChanged = true;
                }
            } else {
                isValid = false;
                hostError.setText(lastValidation.getErrorMessage());
                hostError.setVisible(true);
            }
            //PORT
            lastValidation = FieldsValidator.portValidator(portFX.getText());
            if (lastValidation.isValid()) {
                if (port == 0 || !(port == Integer.parseInt(portFX.getText()))) {
                    port = Integer.parseInt(portFX.getText());
                    urlChanged = true;
                }
            } else {
                isValid = false;
                portError.setText(lastValidation.getErrorMessage());
                portError.setVisible(true);
            }

            //LOGIN
            lastValidation = FieldsValidator.hostValidator(loginFX.getText());
            if (lastValidation.isValid()) {
                if (Strings.isNullOrEmpty(login) || !login.equals(loginFX.getText())) {
                    login = loginFX.getText();
                    urlChanged = true;
                }
            } else {
                isValid = false;
                loginError.setText(lastValidation.getErrorMessage());
                loginError.setVisible(true);
            }

            //PASSWORD
            lastValidation = FieldsValidator.hostValidator(passwordFX.getText());
            if (lastValidation.isValid()) {
                if (Strings.isNullOrEmpty(password) || !password.equals(passwordFX.getText())) {
                    password = passwordFX.getText();
                    urlChanged = true;
                }
            } else {
                isValid = false;
                passwordError.setText(lastValidation.getErrorMessage());
                passwordError.setVisible(true);
            }
        } catch (NumberFormatException e) {
            throw new InvalidFieldValueException(e);
        }
        return isValid;
    }

    private void clearInfoFields() {
        hostError.setVisible(false);
        portError.setVisible(false);
        loginError.setVisible(false);
        passwordError.setVisible(false);
        infoFx.setVisible(false);
    }

    private void exitScene() {
        Stage stage = (Stage) loginFX.getScene().getWindow();
        stage.close();
    }

}