package com.techno.mpm.exception;

@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException {

	
	public UserNotFoundException(String message) {
		super(message);
	}

}
