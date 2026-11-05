package com.gui.jhand.core.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;

import static com.gui.jhand.core.web.GlobalExceptionHandler.UNEXPECTED_ERROR_DETAIL;
import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

	private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

	@Test
	void should_handle_illegal_argument_as_bad_request() {
		ProblemDetail problem = handler.handleIllegalArgument(new IllegalArgumentException("Unknown suit symbol: x"));

		assertThat(problem.getStatus()).isEqualTo(400);
		assertThat(problem.getTitle()).isEqualTo("Bad Request");
		assertThat(problem.getDetail()).isEqualTo("Unknown suit symbol: x");
	}

	@Test
	void should_handle_unexpected_exception_without_leaking_its_message() {
		ProblemDetail problem = handler.handleUnexpected(new IllegalStateException("db password is postgres"));

		assertThat(problem.getStatus()).isEqualTo(500);
		assertThat(problem.getTitle()).isEqualTo("Internal Server Error");
		assertThat(problem.getDetail()).isEqualTo(UNEXPECTED_ERROR_DETAIL).doesNotContain("postgres");
	}

}
