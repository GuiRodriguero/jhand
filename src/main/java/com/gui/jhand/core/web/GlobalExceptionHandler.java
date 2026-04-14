package com.gui.jhand.core.web;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = INTERNAL_SERVER_ERROR)
	public ProblemDetail handleAllExceptions(Exception ex) {

		ProblemDetail problem = ProblemDetail.forStatus(500);
		problem.setTitle("Internal Server Error");
		problem.setDetail("Request error: " + ex.getMessage());

		return problem;
	}

}
