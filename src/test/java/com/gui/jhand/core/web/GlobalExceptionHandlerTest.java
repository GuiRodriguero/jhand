package com.gui.jhand.core.web;

import com.gui.jhand.core.exception.BusinessException;
import com.gui.jhand.core.exception.ResourceNotFoundException;
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
	void should_handle_business_exception_as_unprocessable_content() {
		ProblemDetail problem = handler.handleBusiness(new BusinessException("Hand 123 has no valid big blind value"));

		assertThat(problem.getStatus()).isEqualTo(422);
		assertThat(problem.getTitle()).isEqualTo("Unprocessable Content");
		assertThat(problem.getDetail()).isEqualTo("Hand 123 has no valid big blind value");
	}

	@Test
	void should_handle_not_found_exception_as_not_found() {
		ProblemDetail problem = handler.handleNotFound(new ResourceNotFoundException("Session abc not found"));

		assertThat(problem.getStatus()).isEqualTo(404);
		assertThat(problem.getTitle()).isEqualTo("Resource Not Found");
		assertThat(problem.getDetail()).isEqualTo("Session abc not found");
	}

	@Test
	void should_handle_unexpected_exception_without_leaking_its_message() {
		ProblemDetail problem = handler.handleUnexpected(new IllegalStateException("db password is postgres"));

		assertThat(problem.getStatus()).isEqualTo(500);
		assertThat(problem.getTitle()).isEqualTo("Internal Server Error");
		assertThat(problem.getDetail()).isEqualTo(UNEXPECTED_ERROR_DETAIL).doesNotContain("postgres");
	}

}
