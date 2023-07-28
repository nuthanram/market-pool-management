package com.techno.mpm.exceptionhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.techno.mpm.exception.CandidateInfoException;
import com.techno.mpm.exception.DataNotFoundException;
import com.techno.mpm.exception.TrainerException;
import com.techno.mpm.response.ErrorResponse;
import com.techno.mpm.response.FailureResponse;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	
	// handleHttpMediaTypeNotSupported : triggers when the JSON is invalid
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
		details.add(builder.toString());
		return ResponseEntity.ok(ErrorResponse.builder().error(true).message(details).build());

	}

	// handleHttpMessageNotReadable : triggers when the JSON is malformed
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
//			String[] errMsg = ex.getMessage().split(":");
//			String string = errMsg[errMsg.length - 4];
//			errMsg[0] + " : " + errMsg[1] + string.substring(0, string.length() - 7) + ")"
		return ResponseEntity.ok(ErrorResponse.builder().error(true).message("Something Went Wrong").build());
	}

	// handleMethodArgumentNotValid : triggers when @Valid fails
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.ok(ErrorResponse.builder().error(true)
				.message(ex.getBindingResult().getFieldErrors().stream().map(error -> {
					String[] split = error.getField().split(".");
					String field = split.length == 1 ? error.getField() : split[split.length - 1];
					return field.substring(0, 1).toUpperCase() + field.substring(1) + " : " + error.getDefaultMessage();
				}).collect(Collectors.toList())).build());
	}

	// handleMissingServletRequestParameter : triggers when there are missing
	// parameters
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.ok(
				ErrorResponse.builder().error(true).message(ex.getParameterName() + " parameter is missing").build());
	}

	// handleMethodArgumentTypeMismatch : triggers when a parameter's type does not
	// match
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		return ResponseEntity.ok(ErrorResponse.builder().error(true).message(ex.getMessage()).build());
	}

	// dataIntegrityViolationException : triggers when @Validated fails
	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<ErrorResponse> dataIntegrityViolationException(DataIntegrityViolationException exception,
			WebRequest request) {
		return ResponseEntity.ok(ErrorResponse.builder().error(true)
				.message(exception.getMostSpecificCause().getMessage().split(" \"")[0]).build());
	}

	// handleNoHandlerFoundException : triggers when the handler method is invalid
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return ResponseEntity.ok(FailureResponse.builder().isError(true).message(
				String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()))
				.build());
	}

	/*
	 * @Override protected ResponseEntity<Object>
	 * handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders
	 * headers, HttpStatus status, WebRequest request) { return
	 * ResponseEntity.status(HttpStatus.BAD_REQUEST)
	 * .body(FailureResponse.builder().isError(Boolean.TRUE)
	 * .message(String.join(" :: ", ex.getAllErrors().stream()
	 * .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.
	 * toList()))) .build()); }
	 */

	@ExceptionHandler(value = CandidateInfoException.class)
	public ResponseEntity<FailureResponse> candidateException(CandidateInfoException exception) {
		return customException(exception);
	}
	
	@ExceptionHandler(value = HibernateException.class)
	public ResponseEntity<FailureResponse> hibernateException(HibernateException exception) {
		return customException(exception);
	}

	@ExceptionHandler(value = JpaSystemException.class)
	public ResponseEntity<FailureResponse> jpaSystemException(JpaSystemException exception) {
		return customException(exception);
	}

	@ExceptionHandler(value = TrainerException.class)
	public ResponseEntity<FailureResponse> trainerException(TrainerException exception) {
		return customException(exception);
	}

	@ExceptionHandler(value = DataNotFoundException.class)
	public ResponseEntity<FailureResponse> dataNotFoundExceptionHandler(Exception exception) {
		return customException(exception);
	}

	@ExceptionHandler(value = PSQLException.class)
	public ResponseEntity<FailureResponse> psqlExceptionExceptionHandler(PSQLException exception) {
		return customException(exception);
	}

	@ExceptionHandler(value = NullPointerException.class)
	public ResponseEntity<FailureResponse> nullPointerException(NullPointerException exception) {
		return customException(exception);
	}

	@ExceptionHandler(value = NoSuchElementException.class)
	public ResponseEntity<FailureResponse> noSuchElementException(NoSuchElementException exception) {
		return customException(exception);
	}

	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<FailureResponse> illegalArgumentException(IllegalArgumentException exception) {
		return customException(exception);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<FailureResponse> exception(Exception exception) {
		return customException(exception);
	}

	private ResponseEntity<FailureResponse> customException(Exception exception) {
		String[] split = exception.getMessage().split("\\d{3}");
		try {
			return ResponseEntity.status(split.length > 1 ? HttpStatus.valueOf(split[1].trim())
					: HttpStatus.valueOf(Integer.parseInt(Optional.ofNullable(exception.getMessage().replace(split[0], ""))
							.filter(x -> !x.equals("")).orElse("400"))))
					.body(FailureResponse.builder().isError(Boolean.TRUE)
							.cause(exception.getCause() == null ? exception.getMessage() : exception.getCause().toString())
							.exception(exception).data(null).message(split[0]).build());
		} catch (Exception e) {
			return ResponseEntity.status(400)
					.body(FailureResponse.builder().isError(Boolean.TRUE)
							.cause(exception.getCause() == null ? exception.getMessage() : exception.getCause().toString())
							.exception(exception).data(null).message(split[0]).build());
		}
	}

}
