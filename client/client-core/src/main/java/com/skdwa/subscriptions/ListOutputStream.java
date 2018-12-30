package com.skdwa.subscriptions;

import com.skdwa.subscriptions.observer.MessageObservable;
import com.skdwa.subscriptions.observer.Observer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ListOutputStream extends OutputStream implements MessageObservable {
	private final List<String> list;
	private List<String> pingList;
	private Map<Observer, List<String>> observersWithMessages = new HashMap<>();

	public ListOutputStream(List<String> list) {
		super();
		this.list = list;
	}

	public void addPingList(List<String> pingList){
		this.pingList = pingList;
	}

	@Override
	public void write(int b) throws IOException {
//        char c = (char) b;
//        if (c == '\n') {
//            list.add(stringBuilder.toString());
//            stringBuilder = new StringBuilder();
//        } else {
//            stringBuilder.append(c);
//        }
	}

	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
		String message = new String(b);
		AtomicBoolean isListenerValue = new AtomicBoolean(false);
		String[] splitted = message.split("@{3}", 2);
		observersWithMessages.forEach((key, value) -> {
			if (value.contains(splitted[0])) {
				log.info("PING UPDATE IN STREAM!!!");
				if (splitted.length == 1) {
					key.update(splitted[0], "");
				} else {
					key.update(splitted[0], splitted[1]);
				}
				isListenerValue.set(true);
			}
		});
		if(!isListenerValue.get()) {
			list.add(message);
		}
	}

	@Override
	public void addObserverToMessage(Observer observer, String message) {
		if (observersWithMessages.containsKey(observer)) {
			List<String> observerListenMessages = observersWithMessages.get(observer);
			if (!observerListenMessages.contains(message)) {
				observerListenMessages.add(message);
				log.info("Message: '{}' listens to {}", message, observer.toString());
			}
		} else {
			observersWithMessages.put(observer, new ArrayList<>(Collections.singleton(message)));
		}
	}

	@Override
	public void removeObserver(Observer observer) {
		observersWithMessages.remove(observer);
	}
}
