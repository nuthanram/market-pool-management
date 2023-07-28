package com.techno.mpm.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techno.mpm.dto.mail.SendingExceptionLogMailRequest;
import com.techno.mpm.response.FailureResponse;
import com.techno.mpm.service.mail.EmailService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoggerFilter extends OncePerRequestFilter {

	ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());;

	
	@Autowired
	private EmailService emailService;

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("${spring.application.environment}")
	private String environment;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
		long startTime = System.currentTimeMillis();
		filterChain.doFilter(requestWrapper, responseWrapper);
		long timeTaken = System.currentTimeMillis() - startTime;
		String requestBody = getStringValue(requestWrapper.getContentAsByteArray(),
				requestWrapper.getCharacterEncoding());
		String responseBody = getStringValue(responseWrapper.getContentAsByteArray(),
				responseWrapper.getCharacterEncoding());
		int status = response.getStatus();
		if (response.getStatus() > 600 && responseBody.length() > 0) {
			FailureResponse readValue = mapper.readValue(responseBody, FailureResponse.class);
			List<StackTraceElement> asList = Arrays.asList(readValue.getException().getStackTrace());
			List<StackTraceElement> arrayList = new ArrayList<>(asList);
//			String stackTraceElement = arrayList.get(0).toString();
			arrayList.remove(0);
			if (!requestBody.equalsIgnoreCase("")) {
				Map<String, Object> readValue2 = mapper.readValue(requestBody,
						new TypeReference<LinkedHashMap<String, Object>>() {
						});
				readValue.setData(readValue2);
			}
//			emailService.sendMailWithLink(MailDto.builder().body(getErrorDetails(
//					SendingExceptionLogMailRequest.builder().applicationName(applicationName)
//							.errorMessage(readValue.getMessage()).hostIp(InetAddress.getLocalHost().getHostAddress())
//							.hostName(InetAddress.getLocalHost().getHostName())
//							.requestUrl(request.getScheme() + "://" + InetAddress.getLocalHost().getHostAddress() + ":"
//									+ request.getServerPort() + requestWrapper.getRequestURI())
//							.timeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")))
//							.environment(environment).stackTraceElement(arrayList).cause(readValue.getCause()).build(),
//					stackTraceElement)).subject("Send Mail To Admin").to("sahidalom124@mailinator.com").build());
			responseWrapper.reset();
			readValue.setException(null);
			readValue.setCause(null);
			responseWrapper.setStatus(status);
			responseWrapper.getWriter().write(mapper.writeValueAsString(readValue));
			responseBody = getStringValue(responseWrapper.getContentAsByteArray(),
					responseWrapper.getCharacterEncoding());
			responseWrapper.setContentType(MediaType.APPLICATION_JSON_VALUE);
		}
		log.info(
				"FINISHED PROCESSING : METHOD={}; REQUESTURI={}; REQUEST PAYLOAD={}; RESPONSE CODE={}; RESPONSE={} TIME TAKEN={}",
				request.getMethod(), request.getRequestURI(), Arrays.stream(requestBody.replace(" ", "").split("\\n"))
						.filter(Objects::nonNull).reduce("", (x, y) -> x.trim() + " " + y),
				status, responseBody, timeTaken);

		responseWrapper.copyBodyToResponse();
	}

	private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
		try {
			return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getErrorDetails(SendingExceptionLogMailRequest sendingExceptionLogMailRequest,
			String stackTraceElement) {
		return "<!DOCTYPE html>\r\n"
				+ "<html lang=\"en\">\r\n"
				+ "  <head>\r\n"
				+ "    <meta charset=\"UTF-8\" />\r\n"
				+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\r\n"
				+ "    <title>Document</title>\r\n"
				+ "    <style></style>\r\n"
				+ "  </head>\r\n"
				+ "<style>\r\n"
				+ "    div {\r\n"
				+ "      margin-top: 5px;\r\n"
				+ "      margin-bottom: 5px;\r\n"
				+ "    }\r\n"
				+ "  </style>\r\n"
				+ "  <body>\r\n"
				+ "    <h3\r\n"
				+ "      style=\"\r\n"
				+ "        background-color: #144955;\r\n"
				+ "        color: #fff;\r\n"
				+ "        padding: 20px;\r\n"
				+ "        text-align: center;\r\n"
				+ "      \">\r\n"
				+ "      APPLICATION SUPPORT NOTIFICATION\r\n"
				+ "    </h3>\r\n"
				+ "\r\n"
				+ "    <div>\r\n"
				+ "      <strong>Application Name: </strong>\n"
				+ applicationName
				+ "    </div>\r\n"
				+ "    <div>\r\n"
				+ "      <strong>Environment: </strong>\n"
				+ environment
				+ "    </div>\r\n"
				+ "    <div>\r\n"
				+ "      <strong>Host IP: </strong>\n"
				+ sendingExceptionLogMailRequest.getHostIp()
				+ "    </div>\r\n"
				+ "    <div>\r\n"
				+ "      <strong>Host Name: </strong>\n"
				+ sendingExceptionLogMailRequest.getHostName()
				+ "    </div>\r\n"
				+ "\r\n"
				+ "    <div style=\"margin-top: 10px; margin-bottom: 10px\">\r\n"
				+ "      <strong>Request URL: </strong>\n"
				+ "<a href='#'>"
				+sendingExceptionLogMailRequest.getRequestUrl()
				+"</a>"
				+ "      <div>\r\n"
				+ "        <strong>Error Message: </strong>\n"
				+ sendingExceptionLogMailRequest.getErrorMessage()
				+ "      </div>\r\n"
				+ "\r\n"
				+ "      <div>\r\n"
				+ "        <strong>Time Stamp: </strong>\n"
				+ sendingExceptionLogMailRequest.getTimeStamp()
				+ "      </div>\r\n"
				+ "\r\n"
				+ "      <div style=\"margin-top: 15px; margin-bottom: 10px\">\r\n"
				+ "        <span style=\"color: red;font-weight: bold\"> Stack Trace:</span><span style=\"font-size: 14px; color:RGB(60, 76, 81)\">"+stackTraceElement+"</span>"
				+ "      <div style=\"margin-left: 5%; color:RGB(60, 76, 81)\">\r\n"
				+        sendingExceptionLogMailRequest.getStackTraceElement().stream().map(x->"<p style='color:RGB(60, 76, 81);font-size: 14px;'>"+x.toString()+"</p>").collect(Collectors.joining())
				+ " </div>\r\n"
				+ "      </div>\r\n"
				+ "\r\n"
				+ "      <div style=\"margin-top: 15px; margin-bottom: 10px\">\r\n"
				+ "        <b style=\"color: red\"> Root Cause:</b>\r\n"
				+ "      <span style=\"color:RGB(60, 76, 81)\">\r\n "
				+ sendingExceptionLogMailRequest.getCause()
				+ "      </span>\r\n"
				+ "      </div>\r\n"
				+ "\r\n"
				+ "    <div\r\n"
				+ "      style=\"\r\n"
				+ "         background-color: #144955;\r\n"
				+ "        color: #fff;\r\n"
				+ "        padding: 10px 50px;\r\n"
				+ "        display: flex;\r\n"
				+ "        justify-content: 'space-between';\r\n"
				+ "        align-items: center;\r\n"
				+ "      \">\r\n"
				+ "      <div style=\"font-size: large; margin:0 0 0 0\">Technoelevate</div>\r\n"
				+ "      <div style=\"font-size: large; margin:0 25% 0 30%\">www.technoelevate.com</div>\r\n"
				+ "      <div style=\"font-size: large; margin:0 0 0 0\">9876545874</div>\r\n"
				+ "    </div>"
				+ "    </div>\r\n"
				+ "  </body>\r\n"
				+ "</html>\r\n"
				+ "";
		
	}

}