package com.gui.jhand.pokersession.web;

import com.gui.jhand.core.exception.ResourceNotFoundException;
import com.gui.jhand.pokersession.PokerSessionQuery;
import com.gui.jhand.pokersession.TournamentPokerSession;
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

	private static final String SESSION_ID = "HH20260222 Ljuba IV - $0.01-$0.02 - USD No Limit Hold'em";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PokerSessionQuery query;

	@Test
	void should_get_sessions() throws Exception {
		TournamentPokerSession session = valid(TournamentPokerSession.class);
		when(query.findAllSessions(isNull(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(session)));

		mockMvc.perform(get("/v1/sessions"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].sessionId").value(session.getSessionId()))
			.andExpect(jsonPath("$.content[0].type").value(session.getType().name()))
			.andExpect(jsonPath("$.content[0].handCount").value(session.getHandCount()))
			.andExpect(jsonPath("$.content[0].startTime").exists())
			.andExpect(jsonPath("$.content[0].buyIn").exists())
			.andExpect(jsonPath("$.content[0].prize").exists())
			.andExpect(jsonPath("$.content[0].finishPosition").value(session.getFinishPosition()))
			.andExpect(jsonPath("$.content[0].realMoney").value(session.getRealMoney()));

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
	void should_get_session_by_id() throws Exception {
		TournamentPokerSession session = valid(TournamentPokerSession.class);
		when(query.findById(SESSION_ID)).thenReturn(session);

		mockMvc.perform(get("/v1/sessions/{sessionId}", SESSION_ID))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.sessionId").value(session.getSessionId()))
			.andExpect(jsonPath("$.type").value(session.getType().name()))
			.andExpect(jsonPath("$.handCount").value(session.getHandCount()))
			.andExpect(jsonPath("$.startTime").exists())
			.andExpect(jsonPath("$.endTime").exists())
			.andExpect(jsonPath("$.finishPosition").value(session.getFinishPosition()));

		verify(query).findById(SESSION_ID);
	}

	@Test
	void should_return_not_found_when_session_does_not_exist() throws Exception {
		when(query.findById(SESSION_ID))
			.thenThrow(new ResourceNotFoundException("Session %s not found".formatted(SESSION_ID)));

		mockMvc.perform(get("/v1/sessions/{sessionId}", SESSION_ID))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.detail").value("Session %s not found".formatted(SESSION_ID)));
	}

	@Test
	void should_return_bad_request_when_type_is_invalid() throws Exception {
		mockMvc.perform(get("/v1/sessions").param("type", "INVALID")).andExpect(status().isBadRequest());

		verifyNoInteractions(query);
	}

}
