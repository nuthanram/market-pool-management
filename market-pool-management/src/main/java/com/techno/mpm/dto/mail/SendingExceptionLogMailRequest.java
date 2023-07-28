package com.techno.mpm.dto.mail;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendingExceptionLogMailRequest {

	
	private Exception exception;

	private String applicationName;

	private String environment;

	private String hostIp;

	private String hostName;

	private String errorMessage;

	private String timeStamp;

	private String requestUrl;
	
	private String cause;
	
	List<StackTraceElement> stackTraceElement;

}