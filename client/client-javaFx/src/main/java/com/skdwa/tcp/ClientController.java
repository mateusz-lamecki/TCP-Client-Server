package com.skdwa.tcp;

import com.google.common.base.Strings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;

//@Slf4j
public class ClientController {

    Connection connection = null;
    boolean isConnected = false;

    @FXML
    private TextField hostFX;
    @FXML
    private TextField portFx;
    @FXML
    private Label connectionInfoFX;
    @FXML
    private Button connectButtonFX;

    @FXML
    private Label mainStatusBarFx;

    @FXML
    private ListView articlesListFX;

    @FXML
    private void connect(Event event) {
        if (isConnected) {
            connection.disconnect();
            connectButtonFX.setText("Disconnected");
            isConnected = false;
            connectButtonFX.setText("Connect");
            return;
        }
        String host = hostFX.getText();
        String port = portFx.getText();
        if (Strings.isNullOrEmpty(host)) {
            logOnScene("Host cannot be empty");
            return;
        }
        if (Strings.isNullOrEmpty(port)) {
            logOnScene("Port cannot be empty");
            return;
        }
        connectionInfoFX.setText("Connecting...");
        connection = new TCPConnection(host, Integer.parseInt(port));
        try {
            connection.connect();
            connectionInfoFX.setText("Connected!");
            connectButtonFX.setText("Disconnect");
            isConnected = true;
            connection.write("ExampleMessage");
            connection.write("ExampleMessage2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void logOnScene(String logMessage) {
        logOnScene(logMessage, 4);
    }

    private void logOnScene(String logMessage, int seconds) {
        final KeyFrame kf1 = new KeyFrame(Duration.seconds(0), e -> mainStatusBarFx.setText(logMessage));
        final KeyFrame kf2 = new KeyFrame(Duration.seconds(seconds), e -> mainStatusBarFx.setText(""));
        final Timeline timeline = new Timeline(kf1, kf2);
        Platform.runLater(timeline::play);
    }
}
