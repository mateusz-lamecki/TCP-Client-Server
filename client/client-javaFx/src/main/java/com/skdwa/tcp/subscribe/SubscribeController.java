package com.skdwa.tcp.subscribe;

import com.skdwa.subscriptions.ResponseException;
import com.skdwa.subscriptions.SubscriptionManager;
import com.skdwa.tcp.SubscriptionItem;
import com.skdwa.tcp.validate.FieldsValidator;
import com.skdwa.tcp.validate.ValidationStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
public class SubscribeController {

	private final SubscriptionManager manager;
	private final List<SubscriptionItem> subscriptionList;

	@FXML
	private VBox mainContainer;

	@FXML
	private TextField subjectNameFX;

	@FXML
	private Label errorMessage;

	@FXML
	private void subscribeSubject() {
		String newSubjectName = subjectNameFX.getText();
		for(SubscriptionItem item : subscriptionList){
			if(item.getSubject().getValue().equals(newSubjectName)){
				errorMessage.setText("The topic is already subscribed!");
				errorMessage.setVisible(true);
				return;
			}
		}
		ValidationStatus status = FieldsValidator.checkSubscriptionSubject(newSubjectName);
		if (status.isValid()) {
			try {
				if (manager.subscribeSubject(newSubjectName)) {
					subscriptionList.add(new SubscriptionItem(newSubjectName, true));
					exit();
				} else {
					errorMessage.setText("Connection problem. Try again later");
					errorMessage.setVisible(true);
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			} catch (ResponseException e) {
				log.info(e.getMessage());
				if(e.getMessage().contains("INVALID_TOPIC")){
					errorMessage.setText("The topic as not created yet");
					errorMessage.setVisible(true);
				}else {
					errorMessage.setText("Unknown problem, try later");
					errorMessage.setVisible(true);
				}
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
