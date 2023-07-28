package com.techno.mpm.auth.exception;

@SuppressWarnings("serial")
public class MessagingException extends RuntimeException {
	

	public MessagingException(String message) {
		super(message);
	}

	public MessagingException(String message, Throwable cause) {
		super(message, cause);
	}

}
