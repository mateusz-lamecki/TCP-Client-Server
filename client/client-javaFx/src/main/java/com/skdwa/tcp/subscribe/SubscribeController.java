package com.skdwa.tcp.subscribe;

import com.skdwa.subscriptions.ResponseException;
import com.skdwa.subscriptions.SubscriptionManager;
import com.skdwa.tcp.validate.FieldsValidator;
import com.skdwa.tcp.validate.ValidationStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class SubscribeController {

	private final SubscriptionManager manager;
	private final ListView<String> subscriptionList;

	@FXML
	private VBox mainContainer;

	@FXML
	private TextField subjectNameFX;

	@FXML
	private Label errorMessage;

	@FXML
	private void subscribeSubject() {
		ValidationStatus status = FieldsValidator.checkSubscriptionSubject(subjectNameFX.getText());
		if (status.isValid()) {
			try {
				manager.subscribeSubject(subjectNameFX.getText());
				subscriptionList.getItems().add(subjectNameFX.getText());
				exit();
			} catch (IOException e) {
				log.error(e.getMessage());
			} catch (ResponseException e) {
				errorMessage.setText(e.getMessage());
				errorMessage.setVisible(true);
			}
		} else {
			errorMessage.setText(status.getErrorMessage());
			errorMessage.setVisible(true);
		}
	}

	@FXML
	private void cancel() {
		exit();
	}


	private void exit() {
		Stage stage = (Stage) mainContainer.getScene().getWindow();
		stage.close();
	}
}
