package com.gui.jhand.core.web;

import com.gui.jhand.core.exception.BusinessException;
import com.gui.jhand.core.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	static final String UNEXPECTED_ERROR_DETAIL = "An unexpected error occurred while processing the request.";

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
		log.warn("Invalid request: {}", ex.getMessage());

		ProblemDetail problem = ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getMessage());
		problem.setTitle("Bad Request");
		return problem;
	}

	@ResponseStatus(UNPROCESSABLE_CONTENT)
	@ExceptionHandler(BusinessException.class)
	public ProblemDetail handleBusiness(BusinessException ex) {
		log.warn("Business rule violated: {}", ex.getMessage());

		ProblemDetail problem = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_CONTENT, ex.getMessage());
		problem.setTitle("Unprocessable Content");
		return problem;
	}

	@ResponseStatus(NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
		log.warn("Resource not found: {}", ex.getMessage());

		ProblemDetail problem = ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
		problem.setTitle("Resource Not Found");
		return problem;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	public ProblemDetail handleUnexpected(Exception ex) {
		log.error("Unexpected error while processing request", ex);

		ProblemDetail problem = ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR_DETAIL);
		problem.setTitle("Internal Server Error");
		return problem;
	}

}
