package com.skdwa.tcp;

import com.google.common.base.Strings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

//@Slf4j
public class ClientController {
    private Session session = new Session();

    private Connection connection = null;
    private boolean isConnected = false;

    @FXML
    private VBox mainContainer;
    @FXML
    private Label mainStatusBarFx;
    @FXML
    private Label welcomeLabel;
    @FXML
    private ListView articlesListFX;

    @FXML
    private void initialize() {
        try {
            setSceneVisibility(false);
            showLoginScene();
            welcomeLabel.setText("Welcome, " + session.getUser());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (!Strings.isNullOrEmpty(session.getToken())) {
                setSceneVisibility(true);
            }
        }
    }

    private void setSceneVisibility(boolean isVisible) {
        mainContainer.setVisible(isVisible);
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

    private void showLoginScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        LoginController loginController = new LoginController(session);
        loader.setController(loginController);
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Login/Register");
        Scene scene = new Scene(root, 850, 750);
        primaryStage.setMinWidth(650);
        primaryStage.setMinHeight(450);
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }
}
