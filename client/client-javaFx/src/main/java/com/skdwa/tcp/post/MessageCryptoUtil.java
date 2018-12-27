package com.skdwa.tcp.post;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageCryptoUtil {

	private static final String LINE_SEPARATOR_ENCODER = "⎹";

	public static String encryptMessage(Post post){
		String messageToEncrypt = post.getTopic() + "$$$" + post.getContent();
		String encryptedMessage = messageToEncrypt.replaceAll("\\R", LINE_SEPARATOR_ENCODER);
		log.debug("Encoded {} to {}", messageToEncrypt, encryptedMessage);
		return encryptedMessage;
	}

	public static Post decryptMessage(String message){
		String decryptedMessage = message.replaceAll(LINE_SEPARATOR_ENCODER, System.lineSeparator());
		log.debug("Decrypted {} to {}", message, decryptedMessage);
		String[] splitted = decryptedMessage.split("\\${3}");
		if(splitted.length != 2){
			throw new IllegalStateException("Message contains more '$$$' than one");
		}
		return new Post(splitted[0], splitted[1]);
	}
}
