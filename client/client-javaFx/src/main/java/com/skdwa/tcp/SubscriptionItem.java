package com.skdwa.tcp;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscriptionItem {
	private StringProperty subject;
	private BooleanProperty isUpdated;

	public SubscriptionItem(String subject, boolean isUpdated){
		this.subject = new SimpleStringProperty(subject);
		this.isUpdated = new SimpleBooleanProperty(isUpdated);
	}
}
