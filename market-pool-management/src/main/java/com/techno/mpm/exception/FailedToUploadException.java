package com.techno.mpm.exception;

@SuppressWarnings("serial")
public class FailedToUploadException extends RuntimeException {

	
	public FailedToUploadException(String msg) {
		super(msg);
	}
}
