package com.gui.jhand.hand.stats.web;

import com.gui.jhand.core.exception.ResourceNotFoundException;
import com.gui.jhand.hand.stats.SessionHandStats;
import com.gui.jhand.hand.stats.SessionHandStatsQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.gui.jhand.TestBase.valid;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionHandStatsRestService.class)
class SessionHandStatsRestServiceTest {

	private static final String SESSION_ID = "HH20260222 Ljuba IV - $0.01-$0.02 - USD No Limit Hold'em";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private SessionHandStatsQuery query;

	@Test
	void should_get_session_hand_stats() throws Exception {
		SessionHandStats stats = valid(SessionHandStats.class);
		when(query.getStats(SESSION_ID)).thenReturn(stats);

		mockMvc.perform(get("/v1/sessions/{sessionId}/hand-stats", SESSION_ID))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.points.length()").value(stats.points().size()))
			.andExpect(jsonPath("$.points[0].handId").value(stats.points().getFirst().handId()))
			.andExpect(jsonPath("$.bestHand.handId").value(stats.bestHand().handId()))
			.andExpect(jsonPath("$.worstHand.handId").value(stats.worstHand().handId()))
			.andExpect(
					jsonPath("$.byPosition[0].position").value(stats.resultsByPosition().getFirst().position().name()))
			.andExpect(jsonPath("$.vpipPercentage").exists())
			.andExpect(jsonPath("$.bbPer100").exists());

		verify(query).getStats(SESSION_ID);
	}

	@Test
	void should_return_not_found_when_session_does_not_exist() throws Exception {
		when(query.getStats(SESSION_ID))
			.thenThrow(new ResourceNotFoundException("Session %s not found".formatted(SESSION_ID)));

		mockMvc.perform(get("/v1/sessions/{sessionId}/hand-stats", SESSION_ID))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.detail").value("Session %s not found".formatted(SESSION_ID)));
	}

}
