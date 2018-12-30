package com.skdwa.tcp;

import com.google.common.base.Strings;
import com.skdwa.subscriptions.ResponseException;
import com.skdwa.subscriptions.SubscriptionManager;
import com.skdwa.subscriptions.observer.Observer;
import com.skdwa.tcp.login.LoginController;
import com.skdwa.tcp.post.NewPostController;
import com.skdwa.tcp.subscribe.SubscribeController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ClientController implements Observer {
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
    private ListView<String> articlesListFX;

    @FXML
    private TableView<SubscriptionItem> subscribedTableViewFX;
    @FXML
    private TableColumn<SubscriptionItem, String> tableViewSubject;
    @FXML
    private TableColumn<SubscriptionItem, Boolean> tableViewUpdated;
    private ObservableList<SubscriptionItem> subscribedSubjectsData = FXCollections.observableArrayList();

    @FXML
    private Button subscribeNewButtonFX;

    @FXML
    private Button unsubscribeSubjectButtonFX;

    @FXML
    private Label newSubjectsAlertFX;

    @FXML
    private void initialize() {
        subscriptionManager.addObserverWithMessage(this, "PING");
		subscribedTableViewFX.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tableViewSubject.setCellValueFactory(cellData -> cellData.getValue().getSubject());
		tableViewUpdated.setCellValueFactory(cellData -> cellData.getValue().getIsUpdated());
		subscribedTableViewFX.setItems(subscribedSubjectsData);
        try {
            setSceneVisibility(false);
            boolean isLogged = logInUser();
            if (!isLogged) {
                ((Stage) mainContainer.getScene().getWindow()).close();
            }
            welcomeLabel.setText("Welcome, " + subscriptionManager.getLoggedUser().getUsername());
            List<String> subscribedSubjects = subscriptionManager.getUserSubscriptions();
            subscribedSubjectsData.addAll(subscribedSubjects
					.stream()
					.map(e -> new SubscriptionItem(e, false))
					.collect(Collectors.toList()));
            mainContainer.resize(680, 600);
        } catch (IOException | ResponseException e) {
            e.printStackTrace();
        } finally {
            if (!Strings.isNullOrEmpty(subscriptionManager.getLoggedUser().getToken())) {
                setSceneVisibility(true);
            }
        }
    }

    @FXML
    private void unsubscribeSelected(){
        List<SubscriptionItem> subscribedList = subscribedTableViewFX.getSelectionModel().getSelectedItems();
        for(SubscriptionItem item : subscribedList){
            try {
                subscriptionManager.unsubscribeSubject(item.getSubject().getValue());
                subscribedSubjectsData.remove(item);
            } catch (IOException e) {
                log.error(e.getMessage());
            } catch (ResponseException e) {
                log.error(e.getMessage());
                logOnScene(e.getMessage(), 10);
            }
        }
    }

    @FXML
    private void subscribeNewSubject() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/subscribe.fxml"));
        SubscribeController subscribeController = new SubscribeController(subscriptionManager, subscribedSubjectsData);
        loader.setController(subscribeController);
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Subscribe new subject");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }

    @FXML
    private void addNewPost() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/newPost.fxml"));
        NewPostController loginController = new NewPostController(subscriptionManager);
        loader.setController(loginController);
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("New Post");
        Scene scene = new Scene(root, 850, 650);
        primaryStage.setMinWidth(650);
        primaryStage.setMinHeight(450);
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }

    @FXML
    private void subscriptionSelected(){
        log.debug("Subscription selected");
        List<SubscriptionItem> items = subscribedTableViewFX.getSelectionModel().getSelectedItems();
        if(items.size() == 1){
            try {
                List<String> messages = subscriptionManager.getAllMessages(items.get(0).getSubject().getValue());
                articlesListFX.getItems().clear();
                articlesListFX.getItems().addAll(messages);
                for (SubscriptionItem item : subscribedSubjectsData) {
                    if(item.equals(items.get(0))){
                        item.setIsUpdated(new SimpleBooleanProperty(false));
                    }
                }
//                subscribedSubjectsData.remove(items.get(0));
//                subscribedSubjectsData.add(new SubscriptionItem(items.get(0).getSubject(), items.get(0).getIsUpdated()));
            } catch (IOException e) {
                log.error(e.getMessage());
            } catch (ResponseException e) {
                logOnScene(e.getMessage());
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
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

	@Override
	public void update(String message, String content) {
        log.info("Received update message", message, content);
        if(message.equalsIgnoreCase("PING")){
            for(SubscriptionItem item :subscribedSubjectsData){
                if(item.getSubject().getValue().equalsIgnoreCase(content)){
                    item.setIsUpdated(new SimpleBooleanProperty(true));
                    int index = subscribedSubjectsData.indexOf(item);
                    subscribedSubjectsData.set(index, item);
                    log.debug("Updated list on {} index with {} value", index, item.getSubject().getValue());
                    break;
                }
            }
        }
	}
}
