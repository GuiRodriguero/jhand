package com.gui.jhand.pokersession.web;

import com.gui.jhand.pokersession.PokerSession;
import com.gui.jhand.pokersession.PokerSessionQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.gui.jhand.TestBase.valid;
import static com.gui.jhand.pokersession.SessionType.CASH_GAME;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PokerSessionRestService.class)
class PokerSessionRestServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PokerSessionQuery query;

	@Test
	void should_get_sessions() throws Exception {
		PokerSession session = valid(PokerSession.class);
		when(query.findAllSessions(isNull(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(session)));

		mockMvc.perform(get("/v1/sessions"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].sessionId").value(session.getSessionId()))
			.andExpect(jsonPath("$.content[0].type").value(session.getType().name()))
			.andExpect(jsonPath("$.content[0].handCount").value(session.getHandCount()))
			.andExpect(jsonPath("$.content[0].startTime").exists());

		verify(query).findAllSessions(isNull(), argThat(pageable -> pageable.getPageSize() == 20
				&& pageable.getSort().getOrderFor("startTime").getDirection() == DESC));
	}

	@Test
	void should_get_sessions_filtered_by_type() throws Exception {
		when(query.findAllSessions(eq(CASH_GAME), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

		mockMvc.perform(get("/v1/sessions").param("type", CASH_GAME.name())).andExpect(status().isOk());

		verify(query).findAllSessions(eq(CASH_GAME), any(Pageable.class));
	}

	@Test
	void should_return_bad_request_when_type_is_invalid() throws Exception {
		mockMvc.perform(get("/v1/sessions").param("type", "INVALID")).andExpect(status().isBadRequest());

		verifyNoInteractions(query);
	}

}
