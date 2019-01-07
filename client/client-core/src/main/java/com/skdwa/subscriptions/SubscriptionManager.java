package com.skdwa.subscriptions;

import com.google.common.base.Strings;
import com.skdwa.subscriptions.observer.MessageObservable;
import com.skdwa.subscriptions.observer.Observer;
import com.skdwa.tcp.Connection;
import com.skdwa.tcp.TCPConnection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

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
	public boolean signUp(String login, String password) throws IOException, LoginIsAlreadyTakenException {
		connectIfNotConnected();
		connection.write("REGISTER@@@" + login + "@@@" + password);
		Response response = waitForResponse("OK", "LOGIN_TAKEN");
		if (response == null) {
			log.info("{} login failed, did not receive response from the server", login);
			return false;
		}
		if (!response.isOk()) {
			throw new LoginIsAlreadyTakenException("login" + " is taken");
		} else {
			loggedUser = new LoggedUser(login, response.getMessage());
			return true;
		}
	}

	public boolean signIn(String login, String password) throws IOException, SignInException {
		connectIfNotConnected();
		connection.write("LOGIN@@@" + login + "@@@" + password);
		byte i = 0;
		Response response = waitForResponse("OK", "INVALID_PASSWORD");
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

	public boolean subscribeSubject(String subjectName) throws IOException, ResponseException {
		if (!isUserLogged()) {
			throw new IllegalStateException("User is not logged");
		}
		connectIfNotConnected();
		connection.write("SUBSCRIBE@@@" + loggedUser.getToken() + "@@@" + subjectName);
		Response response = waitForResponse(Collections.singletonList("OK"), new ArrayList<>(Arrays.asList("INVALID_TOKEN", "INVALID_TOPIC")));
		if (response == null) {
			log.info("Did not receive response from the server");
			return false;
		}
		if (!response.isOk()) {
			throw new ResponseException(response.getMessage());
		} else {
            return true;
		}
	}

	public boolean unsubscribeSubject(String subjectName) throws IOException, ResponseException {
		if (!isUserLogged()) {
			throw new IllegalStateException("User is not logged");
		}
		connectIfNotConnected();
		connection.write("UNSUBSCRIBE@@@" + loggedUser.getToken() + "@@@" + subjectName);
		Response response = waitForResponse(Collections.singletonList("OK"), new ArrayList<>(Arrays.asList("INVALID_TOKEN", "INVALID_TOPIC")));
		if (response == null) {
			log.info("Did not receive response from the server");
			return false;
		}
		if (!response.isOk()) {
			throw new ResponseException(response.getMessage());
		} else {
			return true;
		}
	}

	public boolean publish(String subject, String content) throws IOException, ResponseException {
		if(!isUserLogged()){
			throw new IllegalStateException("User is not logged");
		}
		connectIfNotConnected();
		connection.write("PUBLISH@@@" + loggedUser.getToken() + "@@@" + subject + "@@@" + content);
		Response response = waitForResponse("OK", "INVALID_TOKEN");
		if (response == null) {
			log.info("Did not receive response from the server");
			return false;
		}
		if (!response.isOk()) {
			throw new ResponseException(response.getMessage());
		} else {
			return true;
		}
	}

	public List<String> getUserSubscriptions() throws IOException, ResponseException {
		if (!isUserLogged()) {
			throw new IllegalStateException("User is not logged");
		}
		connectIfNotConnected();
		connection.write("READ_TOPICS@@@" + loggedUser.getToken());
		Response response = waitForResponse("OK", "INVALID_TOKEN");
		if (response == null) {
			log.info("Did not receive response from the server");
			return null;
		}
		if (!response.isOk()) {
			throw new ResponseException("Invalid token");
		} else {
			if(response.getMessage().length() == 0){
				return new ArrayList<>();
			}
			String[] subjects = response.getMessage().split("@{3}");
			return new ArrayList<>(Arrays.asList(subjects));
		}
	}

	public List<String> getAllMessages(String subject) throws IOException, ResponseException {
		if (!isUserLogged()) {
			throw new IllegalStateException("User is not logged");
		}
		connectIfNotConnected();
		connection.write("READ_MESSAGES@@@" + subject);
		Response response = waitForResponse("OK", "INVALID_TOPIC");
		if (response == null) {
			log.info("Did not receive response from the server");
			return null;
		}
		if (!response.isOk()) {
			throw new ResponseException(response.getMessage());
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

	public void addObserverWithMessage(Observer observer, String message){
		MessageObservable observable = (MessageObservable) this.outputStream;
		observable.addObserverToMessage(observer, message);
	}

	private Response waitForResponse(String okMessage, String wrongMessage){
		return waitForResponse(okMessage, wrongMessage, -1);
	}

	private Response waitForResponse(String okMessage, String wrongMessage, int numberOfResponseMessages) {
		return waitForResponse(new ArrayList<>(Collections.singletonList(okMessage)), new ArrayList<>(Collections.singletonList(wrongMessage)), numberOfResponseMessages);
	}

	private Response waitForResponse(List<String> okMessages, List<String> wrongMessages){
		return waitForResponse(okMessages, wrongMessages, -1);
	}
	private Response waitForResponse(List<String> okMessages, List<String> wrongMessages, int numberOfResponseMessages) {
		int i = 0;
		while (i++ < 10) {
			for (int l = 0; l < messages.size(); l++) {
				String message = messages.get(l);
				if (okMessages.stream().anyMatch(message::contains)) {
					String[] splitted = message.split("@@@",2);
					if (numberOfResponseMessages > 0 && splitted.length != numberOfResponseMessages) {
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
				if (wrongMessages.stream().anyMatch(message::contains)) {
					messages.remove(l);
					return new Response(false, message);
				}
			}
			try {
				Thread.sleep(300);
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
