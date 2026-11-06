package com.gui.jhand.hand.web;

import com.gui.jhand.hand.HandResultQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HandResultRestService.class)
class HandResultRestServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private HandResultQuery query;

	@Test
	void should_get_hands() throws Exception {
		mockMvc.perform(get("/v1/hands")).andExpect(status().isOk());
		verify(query).getHandsPaginated(any(HandResultFilter.class), any(Pageable.class));
	}

	@Test
	void should_return_bad_request_when_filter_is_invalid() throws Exception {
		when(query.getHandsPaginated(any(HandResultFilter.class), any(Pageable.class)))
			.thenThrow(new IllegalArgumentException("No enum constant HandRank.INVALID"));

		mockMvc.perform(get("/v1/hands").param("handRank", "INVALID"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.title").value("Bad Request"));
	}

	@Test
	void should_return_internal_server_error_without_leaking_message() throws Exception {
		when(query.getHandsPaginated(any(HandResultFilter.class), any(Pageable.class)))
			.thenThrow(new IllegalStateException("connection refused to localhost:5432"));

		mockMvc.perform(get("/v1/hands"))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.detail").value("An unexpected error occurred while processing the request."));
	}

}
