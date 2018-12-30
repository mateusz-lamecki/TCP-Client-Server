package com.skdwa.subscriptions.observer;

public interface MessageObservable {
	void addObserverToMessage(Observer observer, String message);
	void removeObserver(Observer observer);
}
