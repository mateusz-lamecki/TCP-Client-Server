package com.skdwa.tcp;

import com.google.common.base.Strings;
import com.skdwa.subscriptions.ResponseException;
import com.skdwa.subscriptions.SubscriptionManager;
import com.skdwa.tcp.signpanel.LoginController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

//@Slf4j
public class ClientController {
    private SubscriptionManager subscriptionManager = new SubscriptionManager();

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
    private ListView<String> subscribedListFX;

    @FXML
    private Button subscribeNewButtonFX;

    @FXML
    private Button unsubscribeSubjectButtonFX;

    @FXML
    private Label newSubjectsAlertFX;

    @FXML
    private void initialize() {
        try {
            setSceneVisibility(false);
            boolean isLogged = logInUser();
            if (!isLogged) {
                ((Stage) mainContainer.getScene().getWindow()).close();
            }
            welcomeLabel.setText("Welcome, " + subscriptionManager.getLoggedUser().getUsername());
            subscribedListFX.getItems().addAll(subscriptionManager.getUserSubscriptions());
        } catch (IOException | ResponseException e) {
            e.printStackTrace();
        } finally {
            if (!Strings.isNullOrEmpty(subscriptionManager.getLoggedUser().getToken())) {
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

    private boolean logInUser() throws IOException {
        showLoginScene();
        return subscriptionManager.getLoggedUser() != null && !Strings.isNullOrEmpty(subscriptionManager.getLoggedUser().getToken());
    }

    private void showLoginScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        LoginController loginController = new LoginController(subscriptionManager);
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

    @FXML
    private void addNewPost(){
        System.out.println("test");
    }
}
