package com.techno.mpm.exception.mail;

@SuppressWarnings("serial")
public class MessagingException extends RuntimeException {

	public MessagingException() {
		super();
	}

	
	public MessagingException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessagingException(String message) {
		super(message);
	}

}
