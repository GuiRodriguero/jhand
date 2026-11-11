package com.gui.jhand.hand.web;

import com.gui.jhand.hand.HandResult;
import com.gui.jhand.hand.HandResultQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.gui.jhand.TestBase.valid;
import static com.gui.jhand.pokersession.SessionType.TOURNAMENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
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
		HandResult hand = valid(HandResult.class);
		when(query.getHandsPaginated(any(HandResultFilter.class), any(Pageable.class)))
			.thenReturn(new PageImpl<>(List.of(hand)));

		mockMvc.perform(get("/v1/hands"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].handId").value(hand.getHandId()));
		verify(query).getHandsPaginated(any(HandResultFilter.class), any(Pageable.class));
	}

	@Test
	void should_get_hands_filtered_by_session_type() throws Exception {
		when(query.getHandsPaginated(any(HandResultFilter.class), any(Pageable.class)))
			.thenReturn(new PageImpl<>(List.of()));

		mockMvc.perform(get("/v1/hands").param("sessionType", "TOURNAMENT")).andExpect(status().isOk());
		verify(query).getHandsPaginated(argThat(filter -> filter.sessionType() == TOURNAMENT), any(Pageable.class));
	}

	@Test
	void should_return_bad_request_when_session_type_is_invalid() throws Exception {
		mockMvc.perform(get("/v1/hands").param("sessionType", "INVALID")).andExpect(status().isBadRequest());
		verifyNoInteractions(query);
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
