package com.skdwa.tcp.post;

import com.skdwa.subscriptions.ResponseException;
import com.skdwa.subscriptions.SubscriptionManager;
import com.skdwa.tcp.validate.FieldsValidator;
import com.skdwa.tcp.validate.ValidationStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class NewPostController {
	private final SubscriptionManager manager;

	@FXML
	private AnchorPane mainContainer;

	@FXML
	private TextField subjectFX;

	@FXML
	private TextArea contentFX;

	@FXML
	private Button cancelButton;

	@FXML
	private Button sendButton;

	@FXML
	private Label errorLabel;

	@FXML
	private void sendButtonAction() {
		ValidationStatus messageStatus = FieldsValidator.checkPostMessage(subjectFX.getText(), contentFX.getText());
		if (messageStatus.isValid()) {
			try {
				String messageToSend = MessageCryptoUtil.encryptMessage(new Post(subjectFX.getText(), contentFX.getText()));
				boolean postSuccessfull = manager.publish(subjectFX.getText(), contentFX.getText());
				if (postSuccessfull) {
					exitScene();
				} else {
					errorLabel.setText("Problem with server connection. Please try again.");
					errorLabel.setVisible(true);
				}
			} catch (IOException e) {
				log.error(e.getMessage());
				errorLabel.setText("Unknown error, please contact with admin");
				errorLabel.setVisible(true);
			} catch (ResponseException e) {
				log.error(e.getMessage());
				errorLabel.setText(e.getMessage());
				errorLabel.setVisible(true);
			}
		} else {
			errorLabel.setText(messageStatus.getErrorMessage());
			errorLabel.setVisible(true);
		}
	}

	@FXML
	private void cancelButtonAction() {
		exitScene();
	}


	private void exitScene() {
		Stage stage = (Stage) mainContainer.getScene().getWindow();
		stage.close();
	}

}
