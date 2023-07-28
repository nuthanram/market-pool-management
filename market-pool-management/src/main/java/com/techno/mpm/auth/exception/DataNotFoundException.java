package com.techno.mpm.auth.exception;

@SuppressWarnings("serial")
public class DataNotFoundException extends RuntimeException {
	

	public DataNotFoundException(String message) {
		super(message);
	}

	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
