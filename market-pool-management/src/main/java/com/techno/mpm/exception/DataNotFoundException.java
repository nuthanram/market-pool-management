package com.techno.mpm.exception;

@SuppressWarnings("serial")
public class DataNotFoundException extends RuntimeException {

	
	public DataNotFoundException(String msg) {
		super(msg);
	}

	public DataNotFoundException() {
		super();
	}

	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
